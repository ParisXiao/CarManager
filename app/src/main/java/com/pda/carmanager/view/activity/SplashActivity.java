package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.config.AccountConfig;

/**
 * Created by Admin on 2017/12/7.
 */

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = null;
                //判断是否登录
                if (AccountConfig.IsLogin) {
                    intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }else {
                    intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGHT);

    }
}
