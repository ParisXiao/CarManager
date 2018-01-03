package com.pda.carmanager.shouhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pda.carmanager.base.BaseApplication;

/**
 * 守护进程管理类
 * Created by lzan13 on 2017/3/9.
 */
public class VMManager {

    private final static String TAG = VMManager.class.getSimpleName();

    private static VMManager instance = null;

    private AppCompatActivity daemonActivity;

    private VMManager() {
    }

    /**
     * 获取单例类实例
     */
    public static VMManager getInstance() {
        if (instance == null) {
            instance = new VMManager();
        }
        return instance;
    }

    /**
     * 启动守护 Activity，其实就是一像素大小的流氓 activity
     */
    public void startDaemonActivity() {
        Log.i(TAG, "startCoreProcess: 启动流氓 Activity");
        BaseApplication.getContext()
                .startActivity(new Intent(BaseApplication.getContext(), VMActivity.class));
    }

    /**
     * 结束流氓的 activity
     */
    public void finishDaemonActivity() {
        Log.i(TAG, "startCoreProcess: 结束流氓 Activity");
        if (daemonActivity != null) {
            daemonActivity.finish();
        }
    }

    /**
     * 启动核心进程
     */
    public void startCoreProcess() {
        Log.i(TAG, "startCoreProcess: 启动核心进程");
        Intent wakeIntent = new Intent(BaseApplication.getContext(), VMCoreService.class);
        BaseApplication.getContext().startService(wakeIntent);
    }

    /**
     * 保存当前启动的一像素 Activity
     */
    public void setDaemonActivity(AppCompatActivity daemonActivity) {
        this.daemonActivity = daemonActivity;
    }
}
