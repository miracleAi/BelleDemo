package com.example.zhulinping.studydemo.coord;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

/**
 * Created by zhulinping on 2017/8/17.
 */

public class CoordUtils {
    private static CoordUtils mInstance;

    public static synchronized CoordUtils getInstance() {
        if (mInstance == null) {
            mInstance = new CoordUtils();
        }
        return mInstance;
    }

    /**
     * 获取屏幕尺寸
     */
    public int[] getScreenSize(Activity context) {
        int[] size = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    /**
     * 获取app区域尺寸
     */
    public Rect getAppSize(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect;
    }

    /**
     * 获取view布局区域尺寸
     */
    public Rect getLayoutViewSize(Activity context) {
        Rect rect = new Rect();
        context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rect);
        return rect;
    }

    /**
     * 获取状态栏高度
     */
    public int getStatusHeight(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        return statusBarHeight;
    }

    /**
     * 获取view静态坐标
     */
    public int[] getStaticPosition(View view) {
        int[] position = new int[6];
        //取得view相对于父布局的上下左右的距离
        int left = view.getLeft();
        position[0] = left;
        int right = view.getRight();
        position[1] = right;
        int top = view.getTop();
        position[2] = top;
        int bottom = view.getBottom();
        position[3] = bottom;
        //返回值为getLeft()+getTranslationX()，当setTranslationX()时getLeft()不变，getX()变。
        view.setTranslationX(10.0f);
        view.setTranslationY(10.0f);
        float x = view.getX();
        float y = view.getY();
        position[4] = (int) x;
        position[5] = (int) y;
        return position;
    }

    /**
     * MotionEvent坐标方法
     */
    public float[] getMotionEvent(MotionEvent event) {
        float[] distance = new float[4];
        //获取触摸位置距离当前view左边和顶部的距离
        float x = event.getX();
        float y = event.getY();
        distance[0] = x;
        distance[1] = y;
        //获取触摸位置距离整个屏幕左边和顶部的距离
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        distance[2] = rawX;
        distance[3] = rawY;
        return distance;
    }

    /**
     * 获取view宽高的方法
     */
    public int[] getViewSize(View view) {
        int[] size = new int[4];
        //layout后有效，返回值是mRight-mLeft，一般会参考measure的宽度（measure可能没用），但不是必须的。
        int w = view.getWidth();
        int h = view.getHeight();
        size[0] = w;
        size[1] = h;
        //返回measure过程得到的mMeasuredWidth值，供layout参考，或许没用。
        int mw = view.getMeasuredWidth();
        int mh = view.getMeasuredHeight();
        size[2] = mw;
        size[3] = mh;
        return size;
    }

    /**
     * 获取View自身可见的坐标区域，坐标以自己的左上角为原点(0,0)，另一点为可见区域右下角相对自己(0,0)点的坐标
     */
    public Rect getLocalRect(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect;
    }

    /**
     * 获取View在屏幕绝对坐标系中的可视区域，坐标以屏幕左上角为原点(0,0)，另一个点为可见区域右下角相对屏幕原点(0,0)点的坐标。
     */
    public Rect getGloabalRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }

    /**
     * 坐标是相对整个屏幕而言，Y坐标为View左上角到屏幕顶部的距离。
     */
    public int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 如果为普通Activity则Y坐标为View左上角到屏幕顶部（此时Window与屏幕一样大）；
     * 如果为对话框式的Activity则Y坐标为当前Dialog模式Activity的标题栏顶部到View左上角的距离
     */
    public int[] getLocationOnWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    /**
     * 水平方向挪动View，offset为正则x轴正向移动，移动的是整个View，getLeft()会变的，自定义View很有用。
     */
    public void offsetX(View view) {
        view.offsetLeftAndRight(10);
    }

    /**
     * 垂直方向挪动View，offset为正则y轴正向移动，移动的是整个View，getTop()会变的，自定义View很有用。
     */
    public void offsetY(View view) {
        view.offsetTopAndBottom(10);
    }

    /**
     *特别注意：View的scrollTo()和scrollBy()是用于滑动View中的内容，而不是改变View的位置；
     * 改变View在屏幕中的位置可以使用offsetLeftAndRight()和offsetTopAndBottom()方法，他会导致getLeft()等值改变。
     * */

    /**
     * 将View中内容（不是整个View）滑动到相应的位置，参考坐标原点为ParentView左上角，x，y为正则向xy轴反方向移动，反之同理。
     */
    public void scrollTo(View view, int x, int y) {
        view.scrollTo(x, y);
    }

    /**
     * 在原来基础上滑动xy
     */
    public void scrollBy(View view, int x, int y) {
        view.scrollBy(x, y);
    }
    /**
     * 实质为scrollTo()，只是只改变X轴滑动。
     * */
    public void setScrollX(View view,int value){
        view.setScrollX(value);
    }
    /**
     * 实质为scrollTo()，只是只改变Y轴滑动。
     * */
    public void setScrolly(View view,int value){
        view.setScrollY(value);
    }
}
