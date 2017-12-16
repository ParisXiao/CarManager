package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ErrorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class ErrorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<ErrorBean> errorBeanList = new ArrayList<>();


    public ErrorAdapter(Context context, List<ErrorBean> errorBeanList) {
        this.context = context;
        this.errorBeanList = errorBeanList;
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
        View view = mInflater.inflate(R.layout.item_adderror, parent, false);
        return new ErrorAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ErrorAdapter.MyViewHolder) holder).text_errorAddress.setText("  " + errorBeanList.get(position).getErrorAddress());
        ((ErrorAdapter.MyViewHolder) holder).text_errorTime.setText("  " + errorBeanList.get(position).getErrorAddress());
        if (errorBeanList.get(position).equals("待处理")) {
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setText("  " + errorBeanList.get(position).getErrorAddress());
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setTextColor(context.getResources().getColor(R.color.red));

        } else if (errorBeanList.get(position).equals("申诉成功")) {
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setText("  " + errorBeanList.get(position).getErrorAddress());
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setTextColor(context.getResources().getColor(R.color.park_blue));

        } else if (errorBeanList.get(position).equals("申诉失败")) {
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setText("  " + errorBeanList.get(position).getErrorAddress());
            ((ErrorAdapter.MyViewHolder) holder).text_errorResult.setTextColor(context.getResources().getColor(R.color.text_msgtime));


        }


    }

    @Override
    public int getItemCount() {
        return errorBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_errorAddress;
        TextView text_errorTime;
        TextView text_errorResult;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_errorAddress = (TextView) itemView.findViewById(R.id.shenshu_address);
            text_errorTime = (TextView) itemView.findViewById(R.id.shenshu_time);
            text_errorResult = (TextView) itemView.findViewById(R.id.shenshu_result);
        }
    }

}
