package com.example.zhulinping.studydemo.floatingManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zhulinping.studydemo.R;

public class FloatingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        findViewById(R.id.show_btn).setOnClickListener(this);
        findViewById(R.id.hide_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(FloatingActivity.this, FloatingService.class);
        switch (view.getId()) {
            case R.id.show_btn:
                intent.putExtra(FloatingService.ACTION, FloatingService.SHOW);
                break;
            case R.id.hide_btn:
                intent.putExtra(FloatingService.ACTION, FloatingService.HIDE);
                break;
        }
        startService(intent);
    }
}
