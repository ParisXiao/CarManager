package com.pda.carmanager.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.config.CommonlConfig;
import com.pda.carmanager.util.PhotoUtils;
import com.pda.carmanager.view.widght.PhotoShowDialog;

import java.io.File;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class AddErrorActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private EditText add_error_carnum;
    private ImageView add_error_camera;
    private ImageView add_error_priview;
    private TextView text_add_error_camera;
    private Button add_error_sure;
    private Button add_error_exit;
    private String IMG_PATH;
    private int flage = 0;
    private static final int PHOTO_CAPTURE = 0x11;// 拍照

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adderror);
        initView();

    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        add_error_carnum = (EditText) findViewById(R.id.add_error_carnum);
        add_error_camera = (ImageView) findViewById(R.id.add_error_camera);
        add_error_priview = (ImageView) findViewById(R.id.add_error_priview);
        text_add_error_camera = (TextView) findViewById(R.id.text_add_error_camera);
        add_error_sure = (Button) findViewById(R.id.add_error_sure);
        add_error_exit = (Button) findViewById(R.id.add_error_exit);

        toolbar_left_btn.setOnClickListener(this);
        add_error_sure.setOnClickListener(this);
        add_error_exit.setOnClickListener(this);
        add_error_camera.setOnClickListener(this);
        add_error_priview.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.add_error);
        IMG_PATH = PhotoUtils.getSDPath(this);
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
        startActivityForResult(intent, PHOTO_CAPTURE);
        flage = CommonlConfig.PHOTO_KEY;
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == PHOTO_CAPTURE) {
            Bitmap bitmap = PhotoUtils.decodeUriAsBitmap(Uri.fromFile(new File(IMG_PATH, "temp.jpg")), PhotoUtils.getBitmapDegree(Uri.fromFile(new File(IMG_PATH, "temp.jpg")).getPath()));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bitmap = PhotoUtils.createWatermark(bitmap);
            add_error_priview.setImageBitmap(bitmap);
            add_error_camera.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.add_error_sure:
                submit();
                break;
            case R.id.add_error_exit:
                finish();
                break;
            case R.id.add_error_camera:
                takePhoto();
                add_error_priview.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void submit() {
        // validate
        String carnum = add_error_carnum.getText().toString().trim();
        if (TextUtils.isEmpty(carnum)) {
            Toast.makeText(this, "请输入车位编号", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
