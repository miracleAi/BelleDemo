package com.example.zhulinping.studydemo.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.zhulinping.studydemo.R;
import com.example.zhulinping.studydemo.forum.ui.PostDetailActivity;
import com.example.zhulinping.studydemo.forum.ui.PostEditActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForumActivity extends AppCompatActivity {

    @BindView(R.id.forum_edit_btn)
    Button forumEditBtn;
    @BindView(R.id.forum_detail_btn)
    Button forumDetailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.forum_edit_btn)
    public void forumEditClick() {
        startActivity(new Intent(ForumActivity.this, PostEditActivity.class));
    }

    @OnClick(R.id.forum_detail_btn)
    public void forumDetailClick() {
        startActivity(new Intent(ForumActivity.this, PostDetailActivity.class));
    }
}
