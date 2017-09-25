package com.example.zhulinping.studydemo.softInput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.zhulinping.studydemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoftInputActivity extends AppCompatActivity {

    @BindView(R.id.input_edt)
    EditText inputEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input);
        ButterKnife.bind(this);
    }
}
