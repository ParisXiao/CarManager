package com.pda.carmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ChargeBean;

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
        ((MyViewHolder) holder).text_charger_carnum.setText(chargeBeanList.get(position).getCarNumber());
        ((MyViewHolder) holder).charger_time.setText(chargeBeanList.get(position).getStopTime());
        ((MyViewHolder) holder).charger_price.setText(chargeBeanList.get(position).getParkPrice());
    }

    @Override
    public int getItemCount() {
        return chargeBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_charger_carnum;
        TextView charger_time;
        TextView charger_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_charger_carnum = (TextView) itemView.findViewById(R.id.text_charger_carnum);
            charger_time = (TextView) itemView.findViewById(R.id.charger_time);
            charger_price = (TextView) itemView.findViewById(R.id.charger_price);
        }
    }

}
