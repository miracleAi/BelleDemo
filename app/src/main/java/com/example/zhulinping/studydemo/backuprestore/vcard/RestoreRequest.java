package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */

import android.net.Uri;

public class RestoreRequest {
    /**
     * Uri to be imported. May have different content than originally given from users, so
     * when displaying user-friendly information (e.g. "importing xxx.vcf"), use
     * {@link #displayName} instead.
     * <p>
     * If this is null {@link #data} contains the byte stream of the vcard.
     */
    public final Uri uri;

    /**
     * Holds the byte stream of the vcard, if {@link #uri} is null.
     */
    public final byte[] data;

    /**
     * String to be displayed to the user to indicate the source of the VCARD.
     */
    public final String displayName;

    /**
     * Can be {@link VCardSourceDetector#PARSE_TYPE_UNKNOWN}.
     */
    public final int estimatedVCardType;

    /**
     * Can be null, meaning no preferable charset is available.
     */
    public final String estimatedCharset;

    /**
     * Assumes that one Uri contains only one version, while there's a (tiny) possibility
     * we may have two types in one vCard.
     * <p>
     * e.g.
     * BEGIN:VCARD
     * VERSION:2.1
     * ...
     * END:VCARD
     * BEGIN:VCARD
     * VERSION:3.0
     * ...
     * END:VCARD
     * <p>
     * We've never seen this kind of a file, but we may have to cope with it in the future.
     */
    public final int vcardVersion;

    /**
     * The count of vCard entries in {@link #uri}. A receiver of this object can use it
     * when showing the progress of import. Thus a receiver must be able to torelate this
     * variable being invalid because of vCard's limitation.
     * <p>
     * vCard does not let us know this count without looking over a whole file content,
     * which means we have to open and scan over {@link #uri} to know this value, while
     * it may not be opened more than once (Uri does not require it to be opened multiple times
     * and may become invalid after its close() request).
     */
    public final int entryCount;

    public RestoreRequest(byte[] data, Uri uri, String displayName, int estimatedType, String estimatedCharset,
                          int vcardVersion, int entryCount) {
        this.data = data;
        this.uri = uri;
        this.displayName = displayName;
        this.estimatedVCardType = estimatedType;
        this.estimatedCharset = estimatedCharset;
        this.vcardVersion = vcardVersion;
        this.entryCount = entryCount;
    }
}