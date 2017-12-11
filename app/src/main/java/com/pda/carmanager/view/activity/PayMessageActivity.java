package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.view.test.ZXingActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class PayMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private TextView pay_money;
    private TextView allpay_money;
    private TextView discount;
    private TextView arrears_money;
    private TextView pay_carnum;
    private TextView pay_start_time;
    private TextView pay_end_time;
    private ImageView img_a_manager;
    private RelativeLayout aipay_manager;
    private ImageView img_a_user;
    private RelativeLayout aipay_user;
    private ImageView img_w_manager;
    private RelativeLayout weixin_manager;
    private ImageView img_w_user;
    private RelativeLayout weixin_user;
    private Button button_pay_sure;
    private String flag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
        initaData();
    }

    private void initaData() {
        toolbar_mid.setText(R.string.title_pay);
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pay_money = (TextView) findViewById(R.id.pay_money);
        allpay_money = (TextView) findViewById(R.id.allpay_money);
        discount = (TextView) findViewById(R.id.discount);
        arrears_money = (TextView) findViewById(R.id.arrears_money);
        pay_carnum = (TextView) findViewById(R.id.pay_carnum);
        pay_start_time = (TextView) findViewById(R.id.pay_start_time);
        pay_end_time = (TextView) findViewById(R.id.pay_end_time);
        img_a_manager = (ImageView) findViewById(R.id.img_a_manager);
        aipay_manager = (RelativeLayout) findViewById(R.id.aipay_manager);
        img_a_user = (ImageView) findViewById(R.id.img_a_user);
        aipay_user = (RelativeLayout) findViewById(R.id.aipay_user);
        img_w_manager = (ImageView) findViewById(R.id.img_w_manager);
        weixin_manager = (RelativeLayout) findViewById(R.id.weixin_manager);
        img_w_user = (ImageView) findViewById(R.id.img_w_user);
        weixin_user = (RelativeLayout) findViewById(R.id.weixin_user);
        button_pay_sure = (Button) findViewById(R.id.button_pay_sure);

        toolbar_left_btn.setOnClickListener(this);
        button_pay_sure.setOnClickListener(this);
        aipay_manager.setOnClickListener(this);
        aipay_user.setOnClickListener(this);
        weixin_manager.setOnClickListener(this);
        weixin_user.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.button_pay_sure:
                if (flag.equals("")){
                    Toast.makeText(PayMessageActivity.this,R.string.choose_paytype,Toast.LENGTH_SHORT).show();
                }else if (flag.equals("asao")){
                    startActivityForResult(new Intent(PayMessageActivity.this, CaptureActivity.class), 0);
                    finish();
                }else  if (flag.equals("aimg")) {
                    Intent intent=new Intent(PayMessageActivity.this,ZXingImageActivity.class);
                    startActivity(intent);
                    finish();
                }else if (flag.equals("wsao")){
                    startActivityForResult(new Intent(PayMessageActivity.this, CaptureActivity.class), 0);
                    finish();
                }else if (flag.equals("wimg")){
                    Intent intent=new Intent(PayMessageActivity.this,ZXingImageActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.aipay_manager:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag="asao";

                break;
            case R.id.aipay_user:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag="aimg";
                break;
            case R.id.weixin_manager:
                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag="wsao";
                break;
            case R.id.weixin_user:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                flag="wimg";
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result");
        }
    }
}
