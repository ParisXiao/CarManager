package com.pda.carmanager.view.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.util.CountDownTimerUtil;
import com.pda.carmanager.util.OKHttpUtil;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class PhoneCodeActivity extends BaseActivity {
    private TextView phonecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecode);
        initView();

    }

    private void initView() {
        phonecode = (TextView) findViewById(R.id.phonecode);
        phonecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OKHttpUtil.GetCodeMessage();
                    }
                }).start();
                CountDownTimerUtil mCountDownTimerUtils = new CountDownTimerUtil(phonecode, 60000, 1000);
                mCountDownTimerUtils.start();
            }
        });

    }
}
