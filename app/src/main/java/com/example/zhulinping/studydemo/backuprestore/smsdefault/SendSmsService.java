package com.example.zhulinping.studydemo.backuprestore.smsdefault;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class SendSmsService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
