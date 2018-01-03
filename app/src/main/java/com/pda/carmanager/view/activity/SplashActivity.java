package com.pda.carmanager.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.posapi.PosApi;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.base.BaseApplication;
import com.pda.carmanager.bean.UpdataBean;
import com.pda.carmanager.inter.UpdataApkInterface;
import com.pda.carmanager.presenter.SplashPresenter;
import com.pda.carmanager.service.ScanService;
import com.pda.carmanager.service.VMSignalService;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.view.inter.ISplashViewInter;
import com.pda.carmanager.view.widght.LoopProgressBar;

import java.io.File;
import java.io.Serializable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Admin on 2017/12/7.
 */

public class SplashActivity extends BaseActivity implements ISplashViewInter {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private Handler handler;
    private PosApi mPosSDK;
    private LoopProgressBar splashProgress;
    private TextView splash_status;
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        if (BaseApplication.isPos) {
            mPosSDK = BaseApplication.getInstance().getPosApi();
            //初始化接口时回调
            mPosSDK.setOnComEventListener(mCommEventListener);
            //获取状态时回调
            mPosSDK.setOnDeviceStateListener(onDeviceStateListener);
            Intent newIntent = new Intent(SplashActivity.this, ScanService.class);
            newIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startService(newIntent);
        }
        splashPresenter = new SplashPresenter(this, this);
        splash_status.setText(getResources().getString(R.string.text_splash_login));
        splashProgress.start();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转
        splashPresenter.updata();

    }

    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {
        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            // TODO Auto-generated method stub
            switch (cmdFlag) {
                case PosApi.POS_INIT:
                    if (state == PosApi.COMM_STATUS_SUCCESS) {
                        Toast.makeText(getApplicationContext(), "打印设备初始化成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "打印设备初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private PosApi.OnDeviceStateListener onDeviceStateListener = new PosApi.OnDeviceStateListener() {
        /**
         * @param state 0-获取状态成功  1-获取状态失败
         * @param version 设备固件版本
         * @param serialNo 设备序列号
         * @param psam1 psam1 状态   0-正常   1-无卡   2-卡错误
         * @param psam2 psam2 状态   0-正常   1-无卡   2-卡错误
         * @param ic IC卡 状态   0-正常   1-无卡   2-卡错误
         * @param swipcard 磁卡状态 o-正常  1-故障
         * @param printer 打印机状态 0-正常  1-缺纸
         */
        public void OnGetState(int state, String version, String serialNo, int psam1, int psam2, int ic, int swipcard, int printer) {
            if (state == PosApi.COMM_STATUS_SUCCESS) {

                StringBuilder sb = new StringBuilder();
                String mPsam1 = null;
                switch (psam1) {
                    case 0:
                        mPsam1 = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPsam1 = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mPsam1 = getString(R.string.state_card_error);
                        break;
                }

                String mPsam2 = null;
                switch (psam2) {
                    case 0:
                        mPsam2 = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPsam2 = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mPsam2 = getString(R.string.state_card_error);
                        break;
                }

                String mIc = null;
                switch (ic) {
                    case 0:
                        mIc = getString(R.string.state_normal);
                        break;
                    case 1:
                        mIc = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mIc = getString(R.string.state_card_error);
                        break;
                }

                String magnetic_card = null;
                switch (swipcard) {
                    case 0:
                        magnetic_card = getString(R.string.state_normal);
                        break;
                    case 1:
                        magnetic_card = getString(R.string.state_fault);
                        break;

                }

                String mPrinter = null;
                switch (printer) {
                    case 0:
                        mPrinter = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPrinter = getString(R.string.state_no_paper);
                        break;

                }

                sb.append(/*getString(R.string.pos_status)+"\n "
                            +*/
                        getString(R.string.psam1_) + mPsam1 + "\n" //pasm1
                                + getString(R.string.psam2) + mPsam2 + "\n" //pasm2
                                + getString(R.string.ic_card) + mIc + "\n" //card
                                + getString(R.string.magnetic_card) + magnetic_card + "\n" //磁条卡
                                + getString(R.string.printer) + mPrinter + "\n" //打印机
                );

                sb.append(getString(R.string.pos_serial_no) + serialNo + "\n");
                sb.append(getString(R.string.pos_firmware_version) + version);
                DialogUtil.showMessage(SplashActivity.this, sb.toString());

            } else {
                // 获取状态失败
                DialogUtil.showMessage(SplashActivity.this, getString(R.string.get_pos_status_failed));
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        splashProgress = (LoopProgressBar) findViewById(R.id.splashProgress);
        splash_status = (TextView) findViewById(R.id.splash_status);
    }

    @Override
    public void needUpdata(final Serializable object) {
        UpdataBean value = (UpdataBean) object;
        showUpdata(value.getUpdataUrl(), value.getAppVersion(), value.getSize());
    }

    private void showUpdata(final String url, final String AppVersion, final String size) {
        int i = DialogUtil.GetNetworkType(SplashActivity.this);
        if (i == 0) {
            splash_status.setText(getResources().getString(R.string.text_splash_net));
        } else if (i == DialogUtil.NETTYPE_WIFI) {
            downLoadApk(url, AppVersion, size);
        } else {
            DialogUtil.showMessageAndEventDialog(SplashActivity.this, "当前网络处于2G/3G/4G，是否继续下载更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    downLoadApk(url, AppVersion, size);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

    protected void downLoadApk(final String url, String AppVersion, String size) {
        deleteApk();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.alpha = 1f;
        window.setAttributes(lp);
        window.setContentView(R.layout.updata_progress);
        TextView updata_progress_versions = (TextView) window.findViewById(R.id.updata_progress_versions);
        TextView updata_progress_size = (TextView) window.findViewById(R.id.updata_progress_size);
        updata_progress_versions.setText("最新版本：v" + AppVersion);
        updata_progress_size.setText("APK大小：" + size);
        final NumberProgressBar updata_progress_numberbar = (NumberProgressBar) window.findViewById(R.id.updata_progress_numberbar);
        new Thread() {
            @Override
            public void run() {
                try {
//
                    File file = DialogUtil.getFileFromServerForNumberProgressBar(url, "updata.apk", new UpdataApkInterface() {
                        @Override
                        public void getCurrentProcestion(final int data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updata_progress_numberbar.setProgress(data);
                                }
                            });
                        }
                    });
                    sleep(300);
                    installApk(file);
                    dialog.dismiss(); //结束掉进度条对话框
                    finish();
                } catch (Exception e) {
                    dialog.dismiss();
                    deleteApk();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "下载失败,请检查网络问题并重新下载",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    Log.d("WelcomeActivity", e.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //删除之前下载的apk文件
    public void deleteApk() {
        File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
        if (file.exists()) {
            file.delete();
        }
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void notUpdata() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashPresenter.splash();
            }
        },SPLASH_DISPLAY_LENGHT);

    }

    @Override
    public void updataFail(String msg) {

    }

    @Override
    public void splashLoginSuccess() {
        splash_status.setText(getResources().getString(R.string.text_splash_com));
        splashProgress.stop();
        //        启动signalA
        Intent intent=new Intent(this, VMSignalService.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
        Intent intent1 = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(intent1);
        SplashActivity.this.finish();

    }

    @Override
    public void splashLoginFail(String msg) {
        splash_status.setText(getResources().getString(R.string.text_splash_com));
        splashProgress.stop();
        Intent intent = new Intent(SplashActivity.this,
                LoginActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
