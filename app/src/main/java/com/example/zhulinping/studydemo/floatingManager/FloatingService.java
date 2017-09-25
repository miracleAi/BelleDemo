package com.example.zhulinping.studydemo.floatingManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FloatingService extends Service {
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    private FloatingView mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = new FloatingView(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action=intent.getStringExtra(ACTION);
            if(SHOW.equals(action)){
                mFloatingView.showFloatingView();
            }else if(HIDE.equals(action)){
                mFloatingView.hideFloatingView();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
