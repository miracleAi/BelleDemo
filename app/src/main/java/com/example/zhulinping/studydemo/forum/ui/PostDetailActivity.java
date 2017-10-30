package com.example.zhulinping.studydemo.forum.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.example.zhulinping.studydemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.post_web)
    SafeWebView mPostWeb;
    @BindView(R.id.comment_et)
    EditText mCommentEt;
    private String url = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        initWebView(mPostWeb, this, true);
        mPostWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mPostWeb.loadUrl(url);
    }

    private void initWebView(WebView view, Context context, boolean supportMultiWindow) {
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setSaveFormData(false);
        settings.setDatabaseEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);

        PackageManager pm = context.getPackageManager();
        boolean supportsMultiTouch = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)
                || pm.hasSystemFeature(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT);
        settings.setDisplayZoomControls(!supportsMultiTouch);
        view.setVerticalScrollBarEnabled(true);
        view.setVerticalScrollbarOverlay(false);
        view.setHorizontalScrollBarEnabled(false);
        view.setHorizontalScrollbarOverlay(false);
        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT < 18) {
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (Build.VERSION.SDK_INT < 17) {
            settings.setEnableSmoothTransition(true);
        }
        if (Build.VERSION.SDK_INT > 16) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (Build.VERSION.SDK_INT < 19) {
            settings.setDatabasePath(context.getCacheDir() + "/databases");
        }
        settings.setAppCacheEnabled(false);
        settings.setAppCachePath(context.getCacheDir().toString());
        settings.setGeolocationDatabasePath(context.getFilesDir().toString());
        settings.setAllowFileAccess(false);
        settings.setSupportZoom(true);
        settings.setAllowContentAccess(true);
        settings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT > 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setSupportMultipleWindows(supportMultiWindow);
    }
}
