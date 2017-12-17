package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.util.CountDownTimerUtil;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class ZXingImageActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private ImageView zxing_img;
    private TextView custTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxingimg);
        initView();
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_mid.setText(R.string.title_user_manager);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        zxing_img = (ImageView) findViewById(R.id.zxing_img);

        toolbar_left_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            byte[] bis = intent.getByteArrayExtra("zxingBitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            zxing_img.setImageBitmap(bitmap);
        }
        custTime = (TextView) findViewById(R.id.custTime);
        custTime.setOnClickListener(this);
        CountDownTimerUtil mCountDownTimerUtils = new CountDownTimerUtil(custTime, 60000, 1000);
        mCountDownTimerUtils.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },60000);
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
