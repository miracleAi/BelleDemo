package com.example.zhulinping.studydemo.forum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.imageselector.utils.ImageSelectorUtils;
import com.example.zhulinping.studydemo.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostEditActivity extends AppCompatActivity implements PhotoAdapter.AddClickListener {
    private static final int REQUEST_CODE = 0x00000011;

    @BindView(R.id.comment_et)
    EditText mCommentEt;
    @BindView(R.id.rv_image)
    RecyclerView mRvImage;

    private PhotoAdapter mAdapter;
    ArrayList<String> mPhotoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PhotoAdapter(this);
        mRvImage.setAdapter(mAdapter);
        mPhotoList.add("");
        mAdapter.refresh(mPhotoList);
        mAdapter.setAddClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if (null != images && images.size() > 0) {
                mPhotoList.clear();
                mPhotoList.addAll(images);
                mPhotoList.add("");
                mAdapter.refresh(mPhotoList);
            }
        }
    }

    @Override
    public void onAddClick() {
        ImageSelectorUtils.openPhoto(PostEditActivity.this, REQUEST_CODE, false, 9);
    }
}
