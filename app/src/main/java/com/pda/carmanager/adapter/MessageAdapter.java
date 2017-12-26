package com.pda.carmanager.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.util.DateUtil;
import com.pda.carmanager.util.HtmlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MessageAdapter extends BaseAdapter {
    private static final int VIEW_TYPE = -1;
    private LayoutInflater mInflater;
    private Context context;
    private List<MsgBean> msgBeenList = new ArrayList<>();


    public MessageAdapter(Context context, List<MsgBean> msgBeenList) {
        this.context = context;
        this.msgBeenList = msgBeenList;
        mInflater = LayoutInflater.from(context);

    }




    @Override
    public int getCount() {
        return msgBeenList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder=null;
        if (viewHolder==null){
            viewHolder=new MyViewHolder();
            convertView= mInflater.inflate(R.layout.item_message, parent, false);
           viewHolder.msg_day = (TextView) convertView.findViewById(R.id.text_item_time);
            viewHolder.msg_hour = (TextView) convertView.findViewById(R.id.text_msg_itemmin);
            viewHolder.msg_title = (TextView) convertView.findViewById(R.id.text_msg_itemtitle);
            viewHolder.msg_content = (TextView) convertView.findViewById(R.id.item_content);
            viewHolder.msg_item = (RelativeLayout) convertView.findViewById(R.id.rel_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (MyViewHolder) convertView.getTag();
        }
        long date = DateUtil.getStringToDate(msgBeenList.get(position).getMsg_time());
        viewHolder.msg_day.setText(DateUtil.getDateToString(date));
        viewHolder.msg_hour.setText(DateUtil.getDateToStringHour(date));
        viewHolder.msg_title.setText(msgBeenList.get(position).getMsg_title());
        viewHolder.msg_content.setText(HtmlUtil.delHTMLTag(Html.fromHtml(msgBeenList.get(position).getMsg_content()).toString()));
        return convertView;
    }

    class MyViewHolder  {

        TextView msg_day;
        TextView msg_hour;
        TextView msg_title;
        TextView msg_content;
        RelativeLayout msg_item;

    }

}
