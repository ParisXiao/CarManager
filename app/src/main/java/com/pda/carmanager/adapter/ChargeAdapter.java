package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.PayMessageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收费记录适配器
 * Created by Administrator on 2017/12/9 0009.
 */

public class ChargeAdapter  extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<ChargeBean> chargeBeanList=new ArrayList<>();


    public ChargeAdapter(Context context,List<ChargeBean> chargeBeanList) {
        this.context=context;
        this.chargeBeanList=chargeBeanList;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return chargeBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder=null;
        if (viewHolder==null){
            viewHolder=new MyViewHolder();
            convertView=mInflater.inflate(R.layout.item_chargerecord, parent, false);
            viewHolder. text_charger_carnum = (TextView) convertView.findViewById(R.id.text_charger_carnum);
            viewHolder.charger_time = (TextView) convertView.findViewById(R.id.charger_stopTime);
            viewHolder. charger_startime = (TextView) convertView.findViewById(R.id.charger_startTime);
            viewHolder.charger_price = (TextView) convertView.findViewById(R.id.charger_price);
            viewHolder.charger_needPay= (Button) convertView.findViewById(R.id.btn_needPay);
            viewHolder. charger_already= (TextView) convertView.findViewById(R.id.text_already);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (MyViewHolder) convertView.getTag();

        }
        viewHolder.charger_time.setText(chargeBeanList.get(position).getStopTime());
        viewHolder.charger_startime.setText(chargeBeanList.get(position).getStartTime());
        viewHolder.charger_price.setText(chargeBeanList.get(position).getParkPrice());
        if (chargeBeanList.get(position).getStatus().equals("1")){
            viewHolder.text_charger_carnum.setText(chargeBeanList.get(position).getCarNumber());
            viewHolder.charger_already.setVisibility(View.VISIBLE);
            viewHolder.charger_already.setText("已 缴");
        }else if (chargeBeanList.get(position).getStatus().equals("3")){
            if (StringEqualUtil.stringNull(chargeBeanList.get(position).getCarNumber())){
                viewHolder.text_charger_carnum.setText(chargeBeanList.get(position).getCarNumber());
                viewHolder.charger_needPay.setVisibility(View.VISIBLE);
            }else {
                viewHolder.text_charger_carnum.setText("未登记车辆");
                viewHolder.charger_already.setVisibility(View.VISIBLE);
                viewHolder.charger_already.setText("欠费");

            }
        }
        viewHolder.charger_needPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayMessageActivity.class);
                intent.putExtra("ID", chargeBeanList.get(position).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class MyViewHolder  {

        TextView text_charger_carnum;
        TextView charger_time;
        TextView charger_startime;
        TextView charger_price;
        TextView charger_already;
        Button charger_needPay;

    }

}
