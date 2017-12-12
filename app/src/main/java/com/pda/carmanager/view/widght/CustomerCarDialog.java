package com.pda.carmanager.view.widght;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pda.carmanager.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class CustomerCarDialog extends Dialog {
    private View customView;
    private Button setBtn;
    private Button cancleBtn;
    private TextView arrow_up;
    private TextView tv01, tv02;
    private ScrollView sv01, sv02;
    private LinearLayout llTimeWheel;
    private DateDialogListener listener;
    private int lastY;
    private int flag;// 标记时分
    private int itemHeight;// 每一行的高度
    private String pHour, pMinute;// 初始化时显示的时分时间
    private String setHour, setMinute;
    private String[] jianchengs={"贵","渝","滇","川","桂","京","津","沪","蒙","新","藏","宁","港",
            "澳","黑","吉","辽","晋","冀","青","鲁","豫","苏","皖","浙",
            "闽","赣","湘","鄂","粤","琼","甘","陕"};
    private String[] zimus={"A","B","C","D","E","F","G","H","J","K","L","M","K",
    "O","P","Q","R","S","T","U","Y","W","X","Y","Z"};

    public CustomerCarDialog(Context context, String jiancheng, String zimu) {
        super(context, R.style.CustomerCarDialog);
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_car_wheel,
                null);
        init(context, jiancheng, zimu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(customView);
    }

    private void init(Context context, final String jiancheng, final String zimu) {
        tv01 = (TextView) customView.findViewById(R.id.tv01);
        tv02 = (TextView) customView.findViewById(R.id.tv02);
        sv01 = (ScrollView) customView.findViewById(R.id.sv01);
        sv02 = (ScrollView) customView.findViewById(R.id.sv02);
        setBtn = (Button) customView.findViewById(R.id.setBtn);
        cancleBtn = (Button) customView.findViewById(R.id.cancleBtn);
        arrow_up = (TextView) customView.findViewById(R.id.arrow_up);
        this.pHour = jiancheng;
        this.pMinute = zimu;
        setHour = pHour;
        setMinute = pMinute;

        llTimeWheel = (LinearLayout) customView
                .findViewById(R.id.ll_time_wheel);
        setHourDial(tv01);
        setMinuteDial(tv02);

        sv01.setOnTouchListener(tListener);
        sv02.setOnTouchListener(tListener);

        final ViewTreeObserver observer = sv01.getViewTreeObserver();// observer
        // 作用当视图完全加载进来的时候再取控件的高度，否则取得值是0
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                int tvHeight = tv02.getHeight();
                itemHeight = tvHeight / 180;
                if (sv01.getViewTreeObserver().isAlive()) {
                    sv01.getViewTreeObserver().removeGlobalOnLayoutListener(
                            this);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, (itemHeight * 3)
                        + arrow_up.getHeight() * 2);
                llTimeWheel.setLayoutParams(params);
                sv01.setLayoutParams(new LinearLayout.LayoutParams(tv02
                        .getWidth(), (itemHeight * 3)));
                sv02.setLayoutParams(new LinearLayout.LayoutParams(tv02
                        .getWidth(), (itemHeight * 3)));
                sv01.scrollTo(0, jianchengs.length * itemHeight);
                sv02.scrollTo(0, zimus.length * itemHeight);

            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSettingDate();
                CustomerCarDialog.this.cancel();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CustomerCarDialog.this.cancel();
            }
        });
    }

    private View.OnTouchListener tListener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            if (v == sv01) {
                flag = 1;
            } else {
                flag = 2;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                final ScrollView sv = (ScrollView) v;
                lastY = sv.getScrollY();
                System.out.println("lastY" + lastY);
                handler.sendMessageDelayed(handler.obtainMessage(0, v), 50);
            }
            return false;
        }
    };

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            ScrollView sv = (ScrollView) msg.obj;

            if (msg.what == 0) {
                if (lastY == sv.getScrollY()) {

                    int num = lastY / itemHeight;
                    int over = lastY % itemHeight;
                    if (over > itemHeight / 2) {// 超过一半滚到下一格
                        locationTo((num + 1) * itemHeight, sv, flag);
                    } else {// 不到一半滚回上一格
                        locationTo(num * itemHeight, sv, flag);
                    }
                } else {
                    lastY = sv.getScrollY();
                    handler.sendMessageDelayed(handler.obtainMessage(0, sv), 50);// 滚动还没停止隔50毫秒再判断
                }
            }

        }

        ;
    };

    private void locationTo(int position, ScrollView scrollview, int flag) {
        switch (flag) {
            case 1:
                int mPosition = 0;
                if (position <= (jianchengs.length-1) * itemHeight) {
                    mPosition = position + jianchengs.length * itemHeight;
                    scrollview.scrollTo(0, mPosition);
                } else if (position >= 2*(jianchengs.length-1)* itemHeight) {
                    mPosition = position - jianchengs.length * itemHeight;
                    scrollview.scrollTo(0, mPosition);
                } else {
                    mPosition = position;
                    scrollview.smoothScrollTo(0, position);
                }
                setHour = jianchengs[(mPosition / itemHeight - jianchengs.length-1) % jianchengs.length-1];
                break;

            case 2:
                int hPosition = 0;
                if (position <= (zimus.length-1) * itemHeight) {
                    hPosition = position + zimus.length * itemHeight;
                    scrollview.scrollTo(0, hPosition);
                } else if (position >= 2*(zimus.length-1) * itemHeight) {
                    hPosition = position - zimus.length * itemHeight;
                    scrollview.scrollTo(0, hPosition);
                } else {
                    hPosition = position;
                    scrollview.smoothScrollTo(0, position);
                }
                setMinute = zimus[(hPosition / itemHeight) % zimus.length + 1];
                break;
        }

    }

    /**
     * 设置地区简称刻度盘
     *
     * @param tv
     */

    private void setMinuteDial(TextView tv) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < zimus.length; j++) {
                buff.append(zimus[j]);
            }

        }

        tv.setText(buff);
    }

    /**
     * 设置字母刻度盘
     *
     * @param tv
     */
    private void setHourDial(TextView tv) {

        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < jianchengs.length; j++) {
                buff.append(jianchengs[j]);
            }
        }

        tv.setText(buff);
    }

    public void setpHour(String pHour) {
        this.pHour = pHour;
    }

    public void setpMinute(String pMinute) {
        this.pMinute = pMinute;
    }

    public void setOnDateDialogListener(DateDialogListener listener) {
        this.listener = listener;
    }

    public interface DateDialogListener {
        void getDate();
    }

    public void getSettingDate() {
        if (listener != null) {
            listener.getDate();
        }
    }

    public String getSettingHour() {
        return setHour;
    }

    public String getSettingMinute() {
        return setMinute;
    }

}

