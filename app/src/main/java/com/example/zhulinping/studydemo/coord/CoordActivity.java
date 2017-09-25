package com.example.zhulinping.studydemo.coord;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.zhulinping.studydemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoordActivity extends AppCompatActivity {

    @BindView(R.id.screen_size_tv)
    TextView screenSizeTv;
    @BindView(R.id.app_size_tv)
    TextView appSizeTv;
    @BindView(R.id.layoutview_size_tv)
    TextView viewSizeTv;
    @BindView(R.id.status_bar_tv)
    TextView statusBarTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coord);
        ButterKnife.bind(this);
    }
    //onWindowFocusChanged ()方法或者之后调用才能取到正确的值
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initView();
    }

    private void initView() {
        int[] screenSize = CoordUtils.getInstance().getScreenSize(this);
        screenSizeTv.setText("width:" + screenSize[0] + "  height:" + screenSize[1]);
        Rect appRect = CoordUtils.getInstance().getAppSize(this);
        appSizeTv.setText("width:" + appRect.width() + "  height:" + appRect.height());
        Rect viewRect = CoordUtils.getInstance().getLayoutViewSize(this);
        viewSizeTv.setText("x:" + viewRect.top + " y:" + viewRect.left + " width:" + viewRect.width() + " height:" + viewRect.height());
        int statusHeight = CoordUtils.getInstance().getStatusHeight(this);
        statusBarTv.setText("height:" + statusHeight);
    }
}
