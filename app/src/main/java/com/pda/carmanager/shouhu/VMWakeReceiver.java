package com.pda.carmanager.shouhu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 接收唤醒应用核心服务的广播接收器，接收守护进程发送过来的广播
 *
 * Created by lzan13 on 2017/3/7.
 */
public class VMWakeReceiver extends BroadcastReceiver {

    private final static String TAG = VMWakeReceiver.class.getSimpleName();

    // 定义守护进程发送唤醒广播的 Action
    public final static String DAEMON_WAKE_ACTION = "com.pda.carmanager.wake";

    @Override public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DAEMON_WAKE_ACTION)) {
            Log.i(TAG, "onReceive: 启动！");
            // 接收到守护进程发送的广播，开始启动核心进程
            VMManager.getInstance().startCoreProcess();
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG, "onReceive: 锁屏！");
            // 检测到锁屏，启动一像素的流氓 Activity
            VMManager.getInstance().startDaemonActivity();
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            Log.i(TAG, "onReceive: 解锁！");
            // 解锁后，结束一像素的流氓 Activity
            VMManager.getInstance().finishDaemonActivity();
        }
    }
}
