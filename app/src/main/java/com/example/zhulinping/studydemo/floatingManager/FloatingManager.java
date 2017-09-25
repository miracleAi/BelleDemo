package com.example.zhulinping.studydemo.floatingManager;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zhulinping on 2017/9/15.
 */

public class FloatingManager {
    private WindowManager mWindowManager;
    private static FloatingManager mFloatingManager;
    private Context mCotext;

    public static FloatingManager getInstance(Context context){
        if(mFloatingManager == null){
            mFloatingManager = new FloatingManager(context);
        }
        return mFloatingManager;
    }

    public  FloatingManager(Context context){
        mCotext = context;
        mWindowManager = (WindowManager) mCotext.getSystemService(Context.WINDOW_SERVICE);
    }

    public boolean addView(View view,WindowManager.LayoutParams parmers){
        try {
            mWindowManager.addView(view,parmers);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean removeView(View view){
        try {
            mWindowManager.removeView(view);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateView(View view,WindowManager.LayoutParams parmers){
        try {
            mWindowManager.updateViewLayout(view,parmers);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
