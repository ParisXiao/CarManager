package com.pda.carmanager.view.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * Created by Admin on 2017/11/29.
 */

public class ZXingActivity extends BaseActivity implements View.OnClickListener {
    private EditText zxing_edit;
    private Button zxing_btn;
    private ImageView zxing_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxingtest);
        initView();
    }

    private void initView() {
        zxing_edit = (EditText) findViewById(R.id.zxing_edit);
        zxing_btn = (Button) findViewById(R.id.zxing_btn);
        zxing_img = (ImageView) findViewById(R.id.zxing_img);

        zxing_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_btn:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String edit = zxing_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap= EncodingUtils.createQRCode(zxing_edit.getText().toString().trim(), 500, 500, logoBitmap);
        zxing_img.setImageBitmap(bitmap);
    }
}
