package com.example.zhulinping.studydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhulinping.studydemo.backuprestore.BackRestoreActivity;
import com.example.zhulinping.studydemo.coord.CoordActivity;
import com.example.zhulinping.studydemo.floatingManager.FloatingActivity;
import com.example.zhulinping.studydemo.forum.ForumActivity;
import com.example.zhulinping.studydemo.statusbar.StatusBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.coord_btn)
    Button mCoordBtn;
    @BindView(R.id.status_bar_btn)
    Button mStatusBtn;
    @BindView(R.id.floating_view_btn)
    Button mFloatingBtn;
    @BindView(R.id.backup_restore_btn)
    Button mBackupRestoreBtn;
    @BindView(R.id.forum_btn)
    Button mForumBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.coord_btn)
    public void coordClick(){
        startActivity(new Intent(MainActivity.this, CoordActivity.class));
    }
    @OnClick(R.id.status_bar_btn)
    public void statusbarClick(){
        startActivity(new Intent(MainActivity.this, StatusBarActivity.class));
    }
    @OnClick(R.id.floating_view_btn)
    public void floatingViewClick(){
        startActivity(new Intent(MainActivity.this, FloatingActivity.class));
    }
    @OnClick(R.id.backup_restore_btn)
    public void backupRestoreClick(){
        startActivity(new Intent(MainActivity.this, BackRestoreActivity.class));
    }
    @OnClick(R.id.forum_btn)
    public void forumClick(){
        startActivity(new Intent(MainActivity.this, ForumActivity.class));
    }
}
