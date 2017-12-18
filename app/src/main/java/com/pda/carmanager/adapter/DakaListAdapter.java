package com.pda.carmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.DakaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class DakaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<DakaBean> dakaList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL = 0x0001;

    public DakaListAdapter(Context context, List<DakaBean> dakaList) {
        inflater = LayoutInflater.from(context);
        this.dakaList = dakaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_trace, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
//            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            itemHolder.dakaTime.setTextColor(0xff555555);
            itemHolder.dakaAddress.setTextColor(0xff555555);
//            itemHolder.tvDot.setBackgroundResource(R.drawable.shape_timelline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
//            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            itemHolder.dakaTime.setTextColor(0xff999999);
            itemHolder.dakaAddress.setTextColor(0xff999999);
//            itemHolder.tvDot.setBackgroundResource(R.drawable.shape_timelline_dot_normal);
        }

        itemHolder.bindHolder(dakaList.get(position));
    }

    @Override
    public int getItemCount() {
        return dakaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dakaTime, dakaAddress;

        //        private TextView tvTopLine, tvDot;
        public ViewHolder(View itemView) {
            super(itemView);
            dakaTime = (TextView) itemView.findViewById(R.id.daka_Time);
            dakaAddress = (TextView) itemView.findViewById(R.id.daka_Address);
//            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
//            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
        }

        public void bindHolder(DakaBean dakaBean) {
            dakaTime.setText(dakaBean.getDakaTime());
            dakaAddress.setText(dakaBean.getDakaAddress());

        }
    }
}

