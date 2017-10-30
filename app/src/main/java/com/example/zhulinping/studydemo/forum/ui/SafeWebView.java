package com.example.zhulinping.studydemo.forum.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author caizhiping
 * @since 2014/5/30
 *
 *        解决WebView组件远程代码执行高危漏洞。
 *        漏洞产生原因是由使用了WebView组件打开网页的时候使用了JAVA桥接接口，但是这个接口没有做安全处理。
 *        处理方法：WebView组件调用时要removeJavascriptInterface删除searchBoxJavaBridge_这个系统默认的java桥接接口，
 *        同时对产品自己开发的JavascriptInterface接口做安全处理。
 */
public class SafeWebView extends WebView {
    public SafeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SafeWebView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        removeSearchBoxImpl();
        try {
            WebSettings settings = getSettings();
            /** 华为的手机需要制定user agent，否则google显示不正常 */
            fixHuaweiUserAgent(settings);
        } catch (Throwable ignore) {
        }
    }

    /** 华为的手机需要制定user agent，否则google显示不正常 */
    public final static void fixHuaweiUserAgent(WebSettings settings) {
        if (Build.VERSION.SDK_INT == 15 && Build.MODEL.contains("HUAWEI")) {
            final String ANDROID_4_0_DEFAULT_USERAGENT_BEGIN_STRING = "Mozilla/5.0 (Linux; U; Android ";
            final String ANDROID_4_0_DEFAULT_USERAGENT_END_STRING = " Build/Android_V01.01) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

            String ua = "";
            String version = android.os.Build.VERSION.RELEASE;
            String language = Locale.getDefault().getLanguage();

            ua = ANDROID_4_0_DEFAULT_USERAGENT_BEGIN_STRING + version + "; " + language + ";"
                    + ANDROID_4_0_DEFAULT_USERAGENT_END_STRING;

            settings.setUserAgentString(ua);
        }
    }

    @SuppressLint("NewApi")
    private boolean removeSearchBoxImpl() {
        try {
            if (Build.VERSION.SDK_INT < 17) {
                invokeMethod("removeJavascriptInterface", "searchBoxJavaBridge_");
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    private void invokeMethod(String methodName, String param) {
        try {
            Method method = WebView.class.getDeclaredMethod(methodName, String.class);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(this, param);
            }
        } catch (Exception ignore) {
        }
    }
    @Override
    public boolean isPrivateBrowsingEnabled() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && getSettings() == null) {
//            java.lang.NullPointerException
//            at android.webkit.WebView.isPrivateBrowsingEnabled(WebView.java:2458)
//            at android.webkit.HTML5Audio$IsPrivateBrowsingEnabledGetter$1.run(HTML5Audio.java:105)
//            at android.os.Handler.handleCallback(Handler.java:605)
//            at android.os.Handler.dispatchMessage(Handler.java:92)
//            at android.os.Looper.loop(Looper.java:137)
//            at android.app.ActivityThread.main(ActivityThread.java:4456)
//            at java.lang.reflect.Method.invokeNative(Native Method)
//            at java.lang.reflect.Method.invoke(Method.java:511)
//            at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:787)
//            at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:554)
//            at dalvik.system.NativeStart.main(Native Method)
            return false; // getSettings().isPrivateBrowsingEnabled()
        } else {
            return super.isPrivateBrowsingEnabled();
        }
    }
}
