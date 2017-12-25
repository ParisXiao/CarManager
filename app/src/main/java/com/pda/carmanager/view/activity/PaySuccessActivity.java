package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by Admin on 2017/12/18.
 */

public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView paySuccess_money;
    private Button btn_paySuccess;
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_paysuccess);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        paySuccess_money = (TextView) findViewById(R.id.paySuccess_money);
        btn_paySuccess = (Button) findViewById(R.id.btn_paySuccess);

        btn_paySuccess.setOnClickListener(this);
        paySuccess_money.setText(getIntent().getStringExtra("payMoney"));
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_mid.setOnClickListener(this);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_left_btn.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        if (getIntent().getStringExtra("payStatus").equals("1")){
            toolbar_mid.setText("支付成功");
        }else {
            toolbar_mid.setText("支付失败");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paySuccess:
                Intent intent=new Intent(this,MyParkActivity.class);
                intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.toolbar_left_btn:
                Intent intent1=new Intent(this,MyParkActivity.class);
                intent1.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent=new Intent(this,MyParkActivity.class);
            intent.addFlags( FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
