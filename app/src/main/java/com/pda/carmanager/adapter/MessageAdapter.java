package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.util.DateUtil;
import com.pda.carmanager.util.HtmlUtil;
import com.pda.carmanager.view.activity.ContentActivity;

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

//    @Override
//    public int getItemViewType(int position) {
//
////        if (position % 2 == 0) {
////            return 1;
////        } else if (position % 3 == 0) {
////            return 2;
////        } else {
////            return 3;
////        }
//        if (msgBeenList.size() <= 0) {
//            return position;
//        }
//        return super.getItemViewType(position);
//    }

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
        long date = DateUtil.getStringToDate(msgBeenList.get(position).getMsg_time());
        ((MyViewHolder) holder).msg_day.setText(DateUtil.getDateToString(date));
        ((MyViewHolder) holder).msg_hour.setText(DateUtil.getDateToStringHour(date));
        ((MyViewHolder) holder).msg_title.setText(msgBeenList.get(position).getMsg_title());
        ((MyViewHolder) holder).msg_content.setText(HtmlUtil.delHTMLTag(Html.fromHtml(msgBeenList.get(position).getMsg_content()).toString()));
        ((MyViewHolder) holder).msg_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ContentActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Title",msgBeenList.get(position).getMsg_title());
                bundle.putString("Id",msgBeenList.get(position).getId());
                bundle.putString("TitleColor",msgBeenList.get(position).getMsg_titleColor());
                bundle.putString("Content",msgBeenList.get(position).getMsg_content());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

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
