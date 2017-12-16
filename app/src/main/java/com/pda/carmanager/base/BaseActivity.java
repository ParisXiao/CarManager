package com.pda.carmanager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.pda.carmanager.R;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.StatusBarUtil;
import com.pda.carmanager.view.activity.MainActivity;


public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏字体颜色
//        StatusBarUtil.transparencyBar(this);
        //将Activity加入管理器
        AMUtil.getManager().putActivity(getClass().getSimpleName(), this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //将Activity移除管理器
        AMUtil.getManager().removeActivity(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AMUtil.getManager().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
