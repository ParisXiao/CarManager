package com.pda.carmanager.view.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * Created by Admin on 2017/11/29.
 */

public class ZXingActivity extends BaseActivity implements View.OnClickListener {
    private EditText zxing_edit;
    private Button zxing_btn;
    private ImageView zxing_img;
    private Button zxingsao_btn;
    private TextView tv_content;

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
        zxingsao_btn = (Button) findViewById(R.id.zxingsao_btn);
        zxingsao_btn.setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_btn:
                submit();
                break;
            case R.id.zxingsao_btn:
                startActivityForResult(new Intent(ZXingActivity.this, CaptureActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result");
            tv_content.setText(result);
        }
    }

    private void submit() {
        // validate
        String edit = zxing_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 位图
        try {
            /**
             * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
             */
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap bitmap = EncodingUtils.createQRCode(zxing_edit.getText().toString().trim(), 500, 500, logoBitmap);
            zxing_img.setImageBitmap(bitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO validate success, do something

    }
}
