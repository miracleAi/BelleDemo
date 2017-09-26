package com.example.zhulinping.studydemo.backuprestore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhulinping.studydemo.R;
import com.example.zhulinping.studydemo.backuprestore.bean.SmsInfo;
import com.example.zhulinping.studydemo.backuprestore.utils.BackupRestoreUtils;
import com.example.zhulinping.studydemo.backuprestore.utils.FileUtils;
import com.example.zhulinping.studydemo.backuprestore.utils.SmsUtils;
import com.example.zhulinping.studydemo.backuprestore.vcard.RestoreRequest;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class BackRestoreActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback {

    @BindView(R.id.btn_backup)
    Button btnBackup;
    @BindView(R.id.tv_backup_path)
    TextView tvBackupPath;
    @BindView(R.id.btn_restore)
    Button btnRestore;
    @BindView(R.id.tv_rstore_path)
    TextView tvRstorePath;
    @BindView(R.id.tv_result)
    TextView tvResult;
    Handler mHandler;
    @BindView(R.id.btn_sms_backup)
    Button btnSmsBackup;
    @BindView(R.id.tv_sms_backup_path)
    TextView tvSmsBackupPath;
    @BindView(R.id.btn_sms_restore)
    Button btnSmsRestore;
    @BindView(R.id.tv_sms_rstore_path)
    TextView tvSmsRstorePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_restore);
        ButterKnife.bind(this);
        btnBackup.setOnClickListener(this);
        btnRestore.setOnClickListener(this);
        btnSmsBackup.setOnClickListener(this);
        btnSmsRestore.setOnClickListener(this);
        mHandler = new Handler(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backup:
                contactsBackup();
                break;
            case R.id.btn_restore:
                contactsRestore();
                break;
            case R.id.btn_sms_backup:
                smsBackup();
                break;
            case R.id.btn_sms_restore:
                setSmsDEfault();
                break;
        }
    }

    public void setSmsDEfault() {
        String currentPn = getPackageName();//获取当前程序包名
        if (!isSmsDefault()) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, currentPn);
            startActivity(intent);
        }else {
            smsRestore();
        }
    }
    public boolean isSmsDefault(){
        String defaultSmsApp = null;
        String currentPn = getPackageName();//获取当前程序包名
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);//获取手机当前设置的默认短信应用的包名
        }
        return defaultSmsApp.equals(currentPn);
    }

    //短信备份
    public void smsBackup() {
        tvSmsBackupPath.setText("sms backup ing ...");
        Task.callInBackground(new Callable<ArrayList<SmsInfo>>() {
            @Override
            public ArrayList<SmsInfo> call() throws Exception {
                return SmsUtils.getInstance().getSmsList(BackRestoreActivity.this);
            }
        }).onSuccess(new Continuation<ArrayList<SmsInfo>, Object>() {
            @Override
            public Object then(Task<ArrayList<SmsInfo>> task) throws Exception {
                final ArrayList<SmsInfo> list = task.getResult();
                if (null == list || list.size() == 0) {
                    tvSmsBackupPath.setText("read sms fail ...");
                    return null;
                }
                Task.callInBackground(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return SmsUtils.getInstance().smsBackup(BackRestoreActivity.this, list);
                    }
                }).onSuccess(new Continuation<String, Object>() {
                    @Override
                    public Object then(Task<String> task) throws Exception {
                        String path = task.getResult();
                        tvSmsBackupPath.setText("sms backup end");
                        if (null == path || "".equals(path)) {
                            tvResult.setText(" sms backup fail");
                            return null;
                        }
                        tvSmsBackupPath.setText(path);
                        tvResult.setText("sms backup success!");
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    //短信还原
    public void smsRestore() {
        tvSmsRstorePath.setText("sms restore ing ...");
        Task.callInBackground(new Callable<ArrayList<SmsInfo>>() {
            @Override
            public ArrayList<SmsInfo> call() throws Exception {
                return SmsUtils.getInstance().readSmsFromFile(BackRestoreActivity.this, FileUtils.getSmsFilePath());
            }
        }).onSuccess(new Continuation<ArrayList<SmsInfo>, Object>() {
            @Override
            public Object then(Task<ArrayList<SmsInfo>> task) throws Exception {
                final ArrayList<SmsInfo> list = task.getResult();
                if (null == list || list.size() == 0) {
                    tvSmsRstorePath.setText("read sms from file fail...");
                    return null;
                }
                Task.callInBackground(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return SmsUtils.getInstance().smsRestore(BackRestoreActivity.this, list);
                    }
                }).onSuccess(new Continuation<Boolean, Object>() {
                    @Override
                    public Object then(Task<Boolean> task) throws Exception {
                        boolean result = task.getResult();
                        tvSmsRstorePath.setText("sms restore done...");
                        if (result) {
                            tvResult.setText("sms restore success !");
                        } else {
                            tvResult.setText("sms restore fail!");
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    //通讯录备份
    public void contactsBackup() {
        tvBackupPath.setText(" contacts backup start");
        Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return BackupRestoreUtils.getInstance().contactsBackup(BackRestoreActivity.this, mBackupListener);
            }
        }).onSuccess(new Continuation<String, Object>() {
            @Override
            public Object then(Task<String> task) throws Exception {
                String path = task.getResult();
                if (null != path && !path.equals("")) {
                    tvBackupPath.setText(path);
                    tvResult.setText("backup success!");
                } else {
                    tvBackupPath.setText("null");
                    tvResult.setText("backup fail");
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    //通讯录还原
    public void contactsRestore() {
        tvRstorePath.setText("contacts restoring ……");
        Task.callInBackground(new Callable<ArrayList<RestoreRequest>>() {
            @Override
            public ArrayList<RestoreRequest> call() throws Exception {
                Log.d("zlp", "uri " + FileUtils.getUri());
                return BackupRestoreUtils.getInstance().getRestoreRequest(BackRestoreActivity.this, new Uri[]{FileUtils.getUri()});
            }
        }).onSuccess(new Continuation<ArrayList<RestoreRequest>, Object>() {
            @Override
            public Object then(Task<ArrayList<RestoreRequest>> task) throws Exception {
                final ArrayList<RestoreRequest> list = task.getResult();
                if (null == list || list.size() == 0) {
                    tvResult.setText("restore fail");
                    return null;
                }
                Task.callInBackground(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return BackupRestoreUtils.getInstance().contactsRestore(BackRestoreActivity.this, list.get(0), mRestoreListener);
                    }
                }).onSuccess(new Continuation<Boolean, Object>() {
                    @Override
                    public Object then(Task<Boolean> task) throws Exception {
                        tvRstorePath.setText(" restore done");
                        boolean result = task.getResult();
                        if (result) {
                            tvResult.setText("restore success");
                        } else {
                            tvResult.setText("restore fail");
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case R.id.contacts_backup_progress:
                tvBackupPath.setText("back progress == " + (int) message.obj);
                break;
            case R.id.contacts_backup_total:
                tvResult.setText("backup total count == " + (int) message.obj);
                break;
            case R.id.contacts_restore_progress:
                tvRstorePath.setText("restore progress == " + (int) message.obj);
                break;
            case R.id.contacts_restore_total:
                tvResult.setText("restore total count == " + (int) message.obj);
                break;
        }
        return false;
    }

    BackupRestoreUtils.BackupListener mBackupListener = new BackupRestoreUtils.BackupListener() {
        @Override
        public void onBackupProgress(int progress) {
            Message msg = Message.obtain();
            msg.what = R.id.contacts_backup_progress;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onTotalCount(int count) {
            Message msg = Message.obtain();
            msg.what = R.id.contacts_backup_total;
            msg.obj = count;
            mHandler.sendMessage(msg);
        }

    };
    BackupRestoreUtils.RestoreListener mRestoreListener = new BackupRestoreUtils.RestoreListener() {
        @Override
        public void onRestoreProgress(int progress) {
            Message msg = Message.obtain();
            msg.what = R.id.contacts_restore_progress;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onTotalCount(int count) {
            Message msg = Message.obtain();
            msg.what = R.id.contacts_restore_total;
            msg.obj = count;
            mHandler.sendMessage(msg);
        }
    };
}
