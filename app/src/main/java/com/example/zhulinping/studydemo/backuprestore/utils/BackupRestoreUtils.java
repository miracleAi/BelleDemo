package com.example.zhulinping.studydemo.backuprestore.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhulinping.studydemo.R;
import com.example.zhulinping.studydemo.backuprestore.BackupReatoreListener;
import com.example.zhulinping.studydemo.backuprestore.vcard.RestoreRequest;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardComposer;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardConfig;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardEntry;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardEntryCommitter;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardEntryConstructor;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardEntryCounter;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardEntryHandler;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardInterpreter;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardParser;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardParser_V21;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardParser_V30;
import com.example.zhulinping.studydemo.backuprestore.vcard.VCardSourceDetector;
import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardException;
import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardNestedException;
import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardNotSupportedException;
import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardVersionException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class BackupRestoreUtils {
    private static final String LOG_TAG = "BackupRestoreUtils";
    public final static int VCARD_VERSION_AUTO_DETECT = 0;
    public final static int VCARD_VERSION_V21 = 1;
    public final static int VCARD_VERSION_V30 = 2;
    private static final String CACHE_FILE_PREFIX = "vcard_import_";
    private int mRestoreCurrentCount = 0;
    private int mRestoreCount = 0;
    private BackupReatoreListener mRestoreListener = null;

    public BackupRestoreUtils() {
    }

    private VCardEntryHandler mHandler = new VCardEntryHandler() {
        @Override
        public void onStart() {

        }

        @Override
        public void onEntryCreated(VCardEntry entry) {
            mRestoreCurrentCount++;
            if (mRestoreListener != null) {
                int progress = mRestoreCurrentCount * 100 / mRestoreCount;
                if (mRestoreCurrentCount % mRestoreCount == 0) {
                    progress = 100;
                }
                if (mRestoreCurrentCount % 5 == 0) {
                    mRestoreListener.onProgress(R.id.contacts_restore_progress, progress);
                }
            }
            Log.d("zlp", "import pregress " + mRestoreCurrentCount);
        }

        @Override
        public void onEnd() {

        }
    };

    //联系人备份
    public String contactsBackup(Context context, BackupReatoreListener listener) {
        String filePath = FileUtils.getContactsFilePath();
        int exportType = VCardConfig.getVCardTypeFromString("default");
        ContentResolver resolver = context.getContentResolver();
        VCardComposer composer = new VCardComposer(context, exportType, true);
        Writer writer = null;
        try {
            OutputStream outputStream = resolver.openOutputStream(FileUtils.getUri());
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            final Uri contentUriForRawContactsEntity = ContactsContract.RawContactsEntity.CONTENT_URI;
            if (!composer.init(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID},
                    null, null,
                    null, contentUriForRawContactsEntity)) {
                String errorReason = composer.getErrorReason();
                Log.d("zlp", "vcard compose fail reason :" + errorReason);
                return null;
            }
            int total = composer.getCount();
            if (total == 0) {
                return null;
            }
            listener.onTotal(R.id.contacts_backup_total, total);
            int current = 1;  // 1-origin
            while (!composer.isAfterLast()) {
                writer.write(composer.createOneEntry());
                // vCard export is quite fast (compared to import), and frequent notifications
                // bother notification bar too much.
                listener.onProgress(R.id.contacts_backup_progress, current);
                current++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.d("zlp", "error" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (composer != null) {
                composer.terminate();
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e("zlp", "IOException is thrown during close(). Ignored. " + e);
                }
            }
        }
        return filePath;
    }

    //联系人还原
    public boolean contactsRestore(Context context, RestoreRequest request, BackupReatoreListener listener) {
        mRestoreCount = 0;
        mRestoreCurrentCount = 0;
        mRestoreListener = listener;
        ContentResolver mResolver = context.getContentResolver();
        final int[] possibleVCardVersions;
        if (request.vcardVersion == VCARD_VERSION_AUTO_DETECT) {
            /**
             * Note: this code assumes that a given Uri is able to be opened more than once,
             * which may not be true in certain conditions.
             */
            possibleVCardVersions = new int[]{
                    VCARD_VERSION_V21,
                    VCARD_VERSION_V30
            };
        } else {
            possibleVCardVersions = new int[]{
                    request.vcardVersion
            };
        }

        final Uri uri = request.uri;
        final int estimatedVCardType = request.estimatedVCardType;
        final String estimatedCharset = request.estimatedCharset;
        final int totalCount = request.entryCount;
        if (totalCount == 0) {
            return false;
        }
        mRestoreCount = totalCount;
        listener.onTotal(R.id.contacts_restore_total, totalCount);
        final VCardEntryConstructor constructor =
                new VCardEntryConstructor(estimatedVCardType, null, estimatedCharset);
        final VCardEntryCommitter committer = new VCardEntryCommitter(mResolver);
        constructor.addEntryHandler(committer);
        constructor.addEntryHandler(mHandler);

        InputStream is = null;
        boolean successful = false;
        try {
            if (uri != null) {
                Log.i(LOG_TAG, "start importing one vCard (Uri: " + uri + ")");
                is = mResolver.openInputStream(uri);
            } else if (request.data != null) {
                Log.i(LOG_TAG, "start importing one vCard (byte[])");
                is = new ByteArrayInputStream(request.data);
            }

            if (is != null) {
                successful = readOneVCard(is, estimatedVCardType, estimatedCharset, constructor,
                        possibleVCardVersions);
            }
        } catch (IOException e) {
            successful = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return successful;
    }

    private boolean readOneVCard(InputStream is, int vcardType, String charset,
                                 final VCardInterpreter interpreter,
                                 final int[] possibleVCardVersions) {
        VCardParser mVCardParser;
        boolean successful = false;
        final int length = possibleVCardVersions.length;
        for (int i = 0; i < length; i++) {
            final int vcardVersion = possibleVCardVersions[i];
            try {
                if (i > 0 && (interpreter instanceof VCardEntryConstructor)) {
                    // Let the object clean up internal temporary objects,
                    ((VCardEntryConstructor) interpreter).clear();
                }

                // We need synchronized block here,
                // since we need to handle mCanceled and mVCardParser at once.
                // In the worst case, a user may call cancel() just before creating
                // mVCardParser.
                synchronized (this) {
                    mVCardParser = (vcardVersion == VCARD_VERSION_V30 ?
                            new VCardParser_V30(vcardType) :
                            new VCardParser_V21(vcardType));
                }
                mVCardParser.parse(is, interpreter);

                successful = true;
                break;
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException was emitted: " + e.getMessage());
            } catch (VCardNestedException e) {
                // This exception should not be thrown here. We should instead handle it
                // in the preprocessing session in ImportVCardActivity, as we don't try
                // to detect the type of given vCard here.
                //
                // TODO: Handle this case appropriately, which should mean we have to have
                // code trying to auto-detect the type of given vCard twice (both in
                // ImportVCardActivity and ImportVCardService).
                Log.e(LOG_TAG, "Nested Exception is found.");
            } catch (VCardNotSupportedException e) {
                Log.e(LOG_TAG, e.toString());
            } catch (VCardVersionException e) {
                if (i == length - 1) {
                    Log.e(LOG_TAG, "Appropriate version for this vCard is not found.");
                } else {
                    // We'll try the other (v30) version.
                }
            } catch (VCardException e) {
                Log.e(LOG_TAG, e.toString());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        return successful;
    }

    //获取联系人还原请求
    public ArrayList<RestoreRequest> getRestoreRequest(Context context, final Uri[] sourceUris) {
        PowerManager.WakeLock mWakeLock;
        final byte[] mSource = null;
        final String mDisplayName;
        final PowerManager powerManager =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE, LOG_TAG);
        mDisplayName = null;
        mWakeLock.acquire();
        try {
            // Uris given from caller applications may not be opened twice: consider when
            // it is not from local storage (e.g. "file:///...") but from some special
            // provider (e.g. "content://...").
            // Thus we have to once copy the content of Uri into local storage, and read
            // it after it.
            //
            // We may be able to read content of each vCard file during copying them
            // to local storage, but currently vCard code does not allow us to do so.
            int cache_index = 0;
            ArrayList<RestoreRequest> requests = new ArrayList<RestoreRequest>();
            if (mSource != null) {
                try {
                    requests.add(constructImportRequest(context, mSource, null, mDisplayName));
                } catch (VCardException e) {
                    Log.e(LOG_TAG, "Maybe the file is in wrong format", e);
                    return null;
                }
            } else {
                final ContentResolver resolver =
                        context.getContentResolver();
                for (Uri sourceUri : sourceUris) {
                    String filename = null;
                    // Note: caches are removed by VCardService.
                    while (true) {
                        filename = CACHE_FILE_PREFIX + cache_index + ".vcf";
                        final File file = context.getFileStreamPath(filename);
                        if (!file.exists()) {
                            break;
                        } else {
                            if (cache_index == Integer.MAX_VALUE) {
                                throw new RuntimeException("Exceeded cache limit");
                            }
                            cache_index++;
                        }
                    }
                    Uri localDataUri = null;

                    try {
                        localDataUri = copyTo(context, sourceUri, filename);
                    } catch (SecurityException e) {
                        Log.e(LOG_TAG, "SecurityException", e);
                        return null;
                    }
                    if (localDataUri == null) {
                        Log.w(LOG_TAG, "destUri is null");
                        break;
                    }

                    String displayName = null;
                    Cursor cursor = null;
                    // Try to get a display name from the given Uri. If it fails, we just
                    // pick up the last part of the Uri.
                    try {
                        cursor = resolver.query(sourceUri,
                                new String[]{OpenableColumns.DISPLAY_NAME},
                                null, null, null);
                        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                            if (cursor.getCount() > 1) {
                                Log.w(LOG_TAG, "Unexpected multiple rows: "
                                        + cursor.getCount());
                            }
                            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            if (index >= 0) {
                                displayName = cursor.getString(index);
                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    if (TextUtils.isEmpty(displayName)) {
                        displayName = sourceUri.getLastPathSegment();
                    }

                    final RestoreRequest request;
                    try {
                        request = constructImportRequest(context, null, localDataUri, displayName);
                    } catch (VCardException e) {
                        Log.e(LOG_TAG, "Maybe the file is in wrong format", e);
                        return null;
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Unexpected IOException", e);
                        return null;
                    }
                    requests.add(request);
                }
            }
            if (!requests.isEmpty()) {
                return requests;
            } else {
                Log.w(LOG_TAG, "Empty import requests. Ignore it.");
                return null;
            }
        } catch (OutOfMemoryError e) {
            Log.e(LOG_TAG, "OutOfMemoryError occured during caching vCard");
            System.gc();
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException during caching vCard", e);
            return null;
        } finally {
            Log.i(LOG_TAG, "Finished caching vCard.");
            mWakeLock.release();
        }
    }

    /**
     * Copy the content of sourceUri to the destination.
     */
    private Uri copyTo(final Context context, final Uri sourceUri, String filename) throws IOException {
        Log.i(LOG_TAG, String.format("Copy a Uri to app local storage (%s -> %s)",
                sourceUri, filename));
        final ContentResolver resolver = context.getContentResolver();
        ReadableByteChannel inputChannel = null;
        WritableByteChannel outputChannel = null;
        Uri destUri = null;
        try {
            inputChannel = Channels.newChannel(resolver.openInputStream(sourceUri));
            destUri = Uri.parse(context.getFileStreamPath(filename).toURI().toString());
            outputChannel = context.openFileOutput(filename, Context.MODE_PRIVATE).getChannel();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
            while (inputChannel.read(buffer) != -1) {
                buffer.flip();
                outputChannel.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                outputChannel.write(buffer);
            }
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG, "Failed to close inputChannel.");
                }
            }
            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG, "Failed to close outputChannel");
                }
            }
        }
        return destUri;
    }

    /**
     * Reads localDataUri (possibly multiple times) and constructs {@link RestoreRequest} from
     * its content.
     *
     * @arg localDataUri Uri actually used for the import. Should be stored in
     * app local storage, as we cannot guarantee other types of Uris can be read
     * multiple times. This variable populates {@link RestoreRequest#uri}.
     * @arg displayName Used for displaying information to the user. This variable populates
     * {@link RestoreRequest#displayName}.
     */
    private RestoreRequest constructImportRequest(Context context, final byte[] data,
                                                  final Uri localDataUri, final String displayName)
            throws IOException, VCardException {
        VCardParser mVCardParser;

        final ContentResolver resolver = context.getContentResolver();
        VCardEntryCounter counter = null;
        VCardSourceDetector detector = null;
        int vcardVersion = VCARD_VERSION_V21;
        try {
            boolean shouldUseV30 = false;
            InputStream is;
            if (data != null) {
                is = new ByteArrayInputStream(data);
            } else {
                is = resolver.openInputStream(localDataUri);
            }
            mVCardParser = new VCardParser_V21();
            try {
                counter = new VCardEntryCounter();
                detector = new VCardSourceDetector();
                mVCardParser.addInterpreter(counter);
                mVCardParser.addInterpreter(detector);
                mVCardParser.parse(is);
            } catch (VCardVersionException e1) {
                try {
                    is.close();
                } catch (IOException e) {
                }

                shouldUseV30 = true;
                if (data != null) {
                    is = new ByteArrayInputStream(data);
                } else {
                    is = resolver.openInputStream(localDataUri);
                }
                mVCardParser = new VCardParser_V30();
                try {
                    counter = new VCardEntryCounter();
                    detector = new VCardSourceDetector();
                    mVCardParser.addInterpreter(counter);
                    mVCardParser.addInterpreter(detector);
                    mVCardParser.parse(is);
                } catch (VCardVersionException e2) {
                    throw new VCardException("vCard with unspported version.");
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }

            vcardVersion = shouldUseV30 ? VCARD_VERSION_V30 : VCARD_VERSION_V21;
        } catch (VCardNestedException e) {
            Log.w(LOG_TAG, "Nested Exception is found (it may be false-positive).");
            // Go through without throwing the Exception, as we may be able to detect the
            // version before it
        }
        return new RestoreRequest(
                data, localDataUri, displayName,
                detector.getEstimatedType(),
                detector.getEstimatedCharset(),
                vcardVersion, counter.getCount());
    }
}
