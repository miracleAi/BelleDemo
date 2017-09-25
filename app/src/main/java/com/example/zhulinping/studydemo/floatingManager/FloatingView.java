package com.example.zhulinping.studydemo.floatingManager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.zhulinping.studydemo.R;

/**
 * Created by zhulinping on 2017/9/15.
 */

public class FloatingView extends FrameLayout{
    private View mFloatingView;
    private ImageView mFloatingimv;
    private FloatingManager mFloatingManager;
    private int mTouchX;
    private int mTouchY;
    private WindowManager.LayoutParams mParmers;
    public FloatingView(@NonNull Context context) {
        super(context);
        mFloatingView = LayoutInflater.from(context).inflate(R.layout.folating_view_layout,null);
        mFloatingimv = mFloatingView.findViewById(R.id.floating_imv);
        mFloatingimv.setOnTouchListener(mOnTouchListener);
        mFloatingManager = FloatingManager.getInstance(context);
    }
    public void showFloatingView(){
        mParmers = new WindowManager.LayoutParams();
        mParmers.gravity = Gravity.TOP | Gravity.LEFT;
        mParmers.x = 0;
        mParmers.y = 100;
        //总是出现在应用程序窗口之上
        mParmers.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParmers.format = PixelFormat.RGBA_8888;
        mParmers.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParmers.width = LayoutParams.WRAP_CONTENT;
        mParmers.height = LayoutParams.WRAP_CONTENT;
        mFloatingManager.addView(mFloatingView, mParmers);
    }
    public void hideFloatingView(){
        mFloatingManager.removeView(mFloatingView);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = (int) motionEvent.getRawX();
                    mTouchY = (int) motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mParmers.x = (int) motionEvent.getRawX();
                    mParmers.y = (int) motionEvent.getRawY();//相对于屏幕左上角的位置
                    mFloatingManager.updateView(mFloatingView, mParmers);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };

}
