package com.pda.carmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.bean.MsgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE = -1;
    private LayoutInflater mInflater;
    private Context context;
    private List<MsgBean> msgBeenList = new ArrayList<>();


    public MessageAdapter(Context context, List<MsgBean> msgBeenList) {
        this.context = context;
        this.msgBeenList = msgBeenList;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {

//        if (position % 2 == 0) {
//            return 1;
//        } else if (position % 3 == 0) {
//            return 2;
//        } else {
//            return 3;
//        }
        if (msgBeenList.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
//        if (VIEW_TYPE == viewType) {
//            view = inflater.inflate(R.layout.item_empty, viewGroup, false);
//
//            return new MyEmptyHolder(view);
//        }
        view = inflater.inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).msg_day.setText(msgBeenList.get(position).getMsg_day());
        ((MyViewHolder) holder).msg_hour.setText(msgBeenList.get(position).getMsg_hour());
        ((MyViewHolder) holder).msg_title.setText(msgBeenList.get(position).getMsg_title());
        ((MyViewHolder) holder).msg_content.setText(msgBeenList.get(position).getMsg_content());
    }

    @Override
    public int getItemCount() {
        return msgBeenList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView msg_day;
        TextView msg_hour;
        TextView msg_title;
        TextView msg_content;
        RelativeLayout msg_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            msg_day = (TextView) itemView.findViewById(R.id.text_item_time);
            msg_hour = (TextView) itemView.findViewById(R.id.text_msg_itemmin);
            msg_title = (TextView) itemView.findViewById(R.id.text_msg_itemtitle);
            msg_content = (TextView) itemView.findViewById(R.id.item_content);
            msg_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
        }
    }

}
