package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.view.widght.JustifyTextView;

/**
 * Created by Admin on 2017/12/14.
 */

public class ContentActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private TextView content_title;
    private JustifyTextView text_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        initData();
    }

    private void initData() {
        Bundle bundle=getIntent().getExtras();
        content_title.setText(bundle.getString("Title"));
        text_content.setText(bundle.getString("Content"));
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content_title = (TextView) findViewById(R.id.content_title);
        text_content = (JustifyTextView) findViewById(R.id.text_content);

        toolbar_left_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.content_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
        }
    }
}
