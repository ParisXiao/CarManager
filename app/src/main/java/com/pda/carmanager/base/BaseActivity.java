package com.pda.carmanager.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.pda.carmanager.util.AMUtil;


public abstract class BaseActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
