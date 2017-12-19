package com.pda.carmanager.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.inter.UpdataApkInterface;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/5/5.
 */

public class DialogUtil {

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int GetNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null && !extraInfo.trim().equals("")) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    private static AlertDialog.Builder alertDialog;

    public static void showMessageAndEventDialog(Context context, String message, DialogInterface.OnClickListener surerListener, DialogInterface.OnClickListener flaseListener) {
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("提示");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("确定", surerListener);
        alertDialog.setNeutralButton("取消", flaseListener);
        alertDialog.create();
        alertDialog.show();
    }

    public static File getFileFromServerForNumberProgressBar(String path, String name, UpdataApkInterface apkInterface) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            final int contentLength = conn.getContentLength();
//            pd.setMax(conn.getContentLength() / 1024);
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            double total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                apkInterface.getCurrentProcestion((int) (total / contentLength * 100));
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        pd.setProgress(finalTotal / contentLength*100);
//                    }
//                });

            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String setFileSize(int size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }

    private static AlertDialog progressDialog;

    public static void dismise() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
        }
    }

    public static void showMessage(Context context, String text) {
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog = new AlertDialog.Builder(context).create();
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
//            WindowManager.LayoutParams params =
//                    dialog.getWindow().getAttributes();
//            params.width = 250;
//            params.height = 250;
//            dialog.getWindow().setAttributes(params);
                Window window = progressDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.alpha = 1f;
                window.setAttributes(lp);
                window.setContentView(R.layout.alert_dialog_layout);
                ProgressWheel progress_wheel = (ProgressWheel) window.findViewById(R.id.progress_wheel);
                TextView progress_wheel_text = (TextView) window.findViewById(R.id.progress_wheel_text);
                progress_wheel_text.setText(text);
            } catch (Exception e) {

            }
        }


    }

    /**
     * 网络设置
     * @param context
     */
    public static void showSetMessage(final Context context) {
        progressDialog = new AlertDialog.Builder(context).create();
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
//            WindowManager.LayoutParams params =
//                    dialog.getWindow().getAttributes();
//            params.width = 250;
//            params.height = 250;
//            dialog.getWindow().setAttributes(params);
                Window window = progressDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.alpha = 1f;
                window.setAttributes(lp);
                window.setContentView(R.layout.layout_choose_dialog);
                TextView text1 = (TextView) window.findViewById(R.id.note_text);
                TextView text2 = (TextView) window.findViewById(R.id.note_text_content);
                Button button1 = (Button) window.findViewById(R.id.note_exit);
                Button button2 = (Button) window.findViewById(R.id.note_sure);
                text1.setText("网络异常");
                text2.setText("是否检查网络设置");
                button1.setText("否");
                button2.setText("是");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.dismise();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.dismise();
                        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                        context.startActivity(intent);

                    }
                });
            } catch (Exception e) {

            }
        }
    }


    public static void showBoXunVIP(Context context, String text,int i) {
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog = new AlertDialog.Builder(context).create();
                progressDialog.show();
//            WindowManager.LayoutParams params =
//                    dialog.getWindow().getAttributes();
//            params.width = 250;
//            params.height = 250;
//            dialog.getWindow().setAttributes(params);
                Window window = progressDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.alpha = 1f;
                window.setAttributes(lp);
                window.setContentView(R.layout.layout_vip_dialog);
                TextView vipNum = (TextView) window.findViewById(R.id.vip_text);
                TextView t1 = (TextView) window.findViewById(R.id.text1);
                TextView t2 = (TextView) window.findViewById(R.id.text2);
                Button vipCom = (Button) window.findViewById(R.id.vip_btn);
                if (i==0){
                    vipNum.setText(text);
                }else {
                    vipNum.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                    t1.setText(text);
                }

                vipCom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {

            }
        }


    }


}
