package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.MyParkActivity;
import com.pda.carmanager.view.activity.PayMessageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class ChargeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<ChargeBean> chargeBeanList=new ArrayList<>();


    public ChargeAdapter(Context context,List<ChargeBean> chargeBeanList) {
        this.context=context;
        this.chargeBeanList=chargeBeanList;
        this.mInflater = LayoutInflater.from(context);

    }

//    @Override
//    public int getItemViewType(int position) {
//
//        if (position % 2 == 0) {
//            return 1;
//        } else if (position % 3 == 0) {
//            return 2;
//        } else {
//            return 3;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_chargerecord, parent, false);
            return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).charger_time.setText(chargeBeanList.get(position).getStopTime());
        ((MyViewHolder) holder).charger_startime.setText(chargeBeanList.get(position).getStartTime());
        ((MyViewHolder) holder).charger_price.setText(chargeBeanList.get(position).getParkPrice());
        if (chargeBeanList.get(position).getStatus().equals("1")){
            ((MyViewHolder) holder).text_charger_carnum.setText(chargeBeanList.get(position).getCarNumber());
            ((MyViewHolder) holder).charger_already.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).charger_already.setText("已 缴");
        }else if (chargeBeanList.get(position).getStatus().equals("3")){
            if (StringEqualUtil.stringNull(chargeBeanList.get(position).getCarNumber())){
                ((MyViewHolder) holder).text_charger_carnum.setText(chargeBeanList.get(position).getCarNumber());
                ((MyViewHolder) holder).charger_needPay.setVisibility(View.VISIBLE);
            }else {
                ((MyViewHolder) holder).text_charger_carnum.setText("未登记车辆");
                ((MyViewHolder) holder).charger_already.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).charger_already.setText("欠费");

            }
        }
        ((MyViewHolder) holder).charger_needPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayMessageActivity.class);
                intent.putExtra("ID", chargeBeanList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chargeBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_charger_carnum;
        TextView charger_time;
        TextView charger_startime;
        TextView charger_price;
        TextView charger_already;
        Button charger_needPay;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_charger_carnum = (TextView) itemView.findViewById(R.id.text_charger_carnum);
            charger_time = (TextView) itemView.findViewById(R.id.charger_stopTime);
            charger_startime = (TextView) itemView.findViewById(R.id.charger_startTime);
            charger_price = (TextView) itemView.findViewById(R.id.charger_price);
            charger_needPay= (Button) itemView.findViewById(R.id.btn_needPay);
            charger_already= (TextView) itemView.findViewById(R.id.text_already);
        }
    }

}
