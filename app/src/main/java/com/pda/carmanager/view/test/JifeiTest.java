package com.pda.carmanager.view.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class JifeiTest extends BaseActivity implements View.OnClickListener {
    private EditText start;
    private EditText end;
    private Button btn_jifei;
    private TextView txet_jifei;
    private int day;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jifei);
        initView();

    }

    private void initView() {
        start = (EditText) findViewById(R.id.start);
        end = (EditText) findViewById(R.id.end);
        btn_jifei = (Button) findViewById(R.id.btn_jifei);
        btn_jifei.setOnClickListener(this);
        txet_jifei = (TextView) findViewById(R.id.txet_jifei);
        txet_jifei.setOnClickListener(this);
    }

    private void submit() {
        // validate
        String startString = start.getText().toString().trim();
        if (TextUtils.isEmpty(startString)) {
            Toast.makeText(this, "开始时间", Toast.LENGTH_SHORT).show();
            return;
        }

        String endString = end.getText().toString().trim();
        if (TextUtils.isEmpty(endString)) {
            Toast.makeText(this, "结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        dateDiff(startString, endString);
        long money = 0;
        long moneylight = 0;
        if (day > 0) {
            money = day * 30;
        }
        if (getHour(startString) > 20 && getHour(startString) < 8) {
            if (getHour(startString) < 24) {
                    moneylight = moneylight + 4 * 2 + 5;
                } else {
                    moneylight = getHour(startString) * 2;
                }

        } else {

        }


    }

    private int getDay(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        try {
// 用parse方法，可能会异常，所以要try-catch
            Date date = format.parse(s);
// 获取日期实例
            calendar = Calendar.getInstance();
// 将日历设置为指定的时间
            calendar.setTime(date);
// 获取年
// 获取天

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getHour(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        try {
// 用parse方法，可能会异常，所以要try-catch
            Date date = format.parse(s);
// 获取日期实例
            calendar = Calendar.getInstance();
// 将日历设置为指定的时间
            calendar.setTime(date);
// 获取年
// 获取天

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getMin(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        try {
// 用parse方法，可能会异常，所以要try-catch
            Date date = format.parse(s);
// 获取日期实例
            calendar = Calendar.getInstance();
// 将日历设置为指定的时间
            calendar.setTime(date);
// 获取年
// 获取天

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.MINUTE);
    }

    public long dateDiff(String startTime, String endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = (int) (diff / nd);// 计算差多少天
            hour = (int) (diff % nd / nh);// 计算差多少小时
            min = (int) (diff % nd % nh / nm);// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            Log.d("Time", "时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_jifei:

                break;
        }
    }
}
