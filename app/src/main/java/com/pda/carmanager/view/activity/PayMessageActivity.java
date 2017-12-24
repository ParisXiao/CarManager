package com.pda.carmanager.view.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.PayCallBackBean;
import com.pda.carmanager.bean.PayInfoBean;
import com.pda.carmanager.presenter.PayInfoPresenter;
import com.pda.carmanager.service.SignalAService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IPayInfoViewInter;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class PayMessageActivity extends BaseActivity implements View.OnClickListener, IPayInfoViewInter, Observer {
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
    private String flag = "";
    private PayInfoPresenter payInfoPresenter;
    private long firstTime = 0;
    private String money = "";
    private String payMoney = "";
    public static boolean flags = false;
    private boolean aorw = false;
    private String Id;
    private SignalAService mService;
    private PayServiceConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
        initaData();
    }

    private void initaData() {
        firstTime = System.currentTimeMillis();
        toolbar_mid.setText(R.string.title_pay);
        DialogUtil.showMessage(this, getResources().getString(R.string.text_loading));
        Id = getIntent().getStringExtra("ID");
        payInfoPresenter = new PayInfoPresenter(this, this);
        payInfoPresenter.getPayInfo(Id);
        conn = new PayServiceConn();
        bindService(new Intent(this, SignalAService.class), conn, BIND_AUTO_CREATE);
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
    public void update(Observable o, Object arg) {
        PayCallBackBean payCallBackBean = (PayCallBackBean) arg;
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra("payStatus", payCallBackBean.getStutas());
        intent.putExtra("payMoney", payCallBackBean.getMoney());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public class PayServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((SignalAService.LocalBinder) iBinder).getService();
            //将当前Activity添加为观察者
            mService.addPayObservable(PayMessageActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flags=false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.button_pay_sure:
                if (System.currentTimeMillis() - firstTime < 1 * 60 * 1000) {
                    if (!flags) {
                        flags = true;
                        if (flag.equals("")) {
                            Toast.makeText(PayMessageActivity.this, R.string.choose_paytype, Toast.LENGTH_SHORT).show();
                            flags = false;
                        } else if (flag.equals("asao")) {
                            aorw = false;

                            startActivityForResult(new Intent(PayMessageActivity.this, CaptureActivity.class), 0);
                        } else if (flag.equals("aimg")) {
                            DialogUtil.showMessage(PayMessageActivity.this, "二维码生成中，请稍后...");
                            payInfoPresenter.Pay(Id, "3", "");
                        } else if (flag.equals("wsao")) {
                            aorw = true;
                            startActivityForResult(new Intent(PayMessageActivity.this, CaptureActivity.class), 0);
                        } else if (flag.equals("wimg")) {
                            DialogUtil.showMessage(PayMessageActivity.this, "二维码生成中，请稍后...");
                            payInfoPresenter.Pay(Id, "1", "");
//
                        }
                    }
                } else {

                    showTimeOutMessage(PayMessageActivity.this);

                }
                break;
            case R.id.aipay_manager:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag = "asao";

                break;
            case R.id.aipay_user:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag = "aimg";
                break;
            case R.id.weixin_manager:
                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                flag = "wsao";
                break;
            case R.id.weixin_user:

                img_a_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_a_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_manager.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_moren));
                img_w_user.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_xuanzhong));
                flag = "wimg";
                break;
        }
    }

    /**
     * 网络设置
     *
     * @param context
     */
    public void showTimeOutMessage(final Context context) {
        final AlertDialog progressDialog = new AlertDialog.Builder(context).create();
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
                window.setContentView(R.layout.layout_timeout_dialog);
                TextView text1 = (TextView) window.findViewById(R.id.note_text);
                Button button2 = (Button) window.findViewById(R.id.note_sure);
                text1.setText("支付超时，请刷新支付信息");
                button2.setText("确定");
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                        payInfoPresenter.getPayInfo(Id);
                        flags = false;

                    }
                });
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            DialogUtil.showMessage(PayMessageActivity.this, "支付中，请稍后...");
            String result = data.getExtras().getString("result");
            Log.d("zxing", result);
            if (aorw) {
                payInfoPresenter.Pay(Id, "2", result);
            } else {
                payInfoPresenter.Pay(Id, "4", result);
            }
        }
    }

    @Override
    public void getSuccess(final PayInfoBean payInfoBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (payInfoBean != null)
                    money = payInfoBean.getCurMoney();
                payMoney = payInfoBean.getTotalMoney();
                pay_money.setText(money);
                allpay_money.setText(payInfoBean.getTotalMoney());
                discount.setText("优惠：" + payInfoBean.getYHMoney());
                arrears_money.setText("欠费：" + payInfoBean.getQFMoney());
                pay_carnum.setText(payInfoBean.getCarNum());
                pay_start_time.setText(payInfoBean.getStartTime() + "至");
                pay_end_time.setText(payInfoBean.getStopTime());
                firstTime = System.currentTimeMillis();
            }
        });
    }

    @Override
    public void paySuccess(String url) {
        if (StringEqualUtil.stringNull(url)) {
//             位图
            try {
                /**
                 * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
                 */
                Intent intent = new Intent(PayMessageActivity.this, ZXingImageActivity.class);
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                Bitmap bitmap = EncodingUtils.createQRCode(url, 400, 400, logoBitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();
                intent.putExtra("zxingBitmap", bitmapByte);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(PayMessageActivity.this);
            AMUtil.actionStart(PayMessageActivity.this, LoginActivity.class);
            finish();
        } else {
            finish();
        }

    }
}
