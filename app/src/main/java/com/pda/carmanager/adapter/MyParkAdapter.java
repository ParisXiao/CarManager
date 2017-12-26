package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MyParkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<MyParkBean> parkBeanList = new ArrayList<>();
    public MyParkAdapter(Context context, List<MyParkBean> parkBeanList) {
        this.context = context;
        this.parkBeanList = parkBeanList;
        mInflater = LayoutInflater.from(context);


    }


    @Override
    public int getCount() {
        return parkBeanList.size();
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
            convertView=mInflater.inflate(R.layout.item_mypark, parent, false);
           viewHolder. item_mypark_btn = (RelativeLayout) convertView.findViewById(R.id.item_mypark_btn);
            viewHolder. text_carType = (TextView) convertView.findViewById(R.id.park_carType);
            viewHolder. img_parkType = (ImageView) convertView.findViewById(R.id.img_park);
            viewHolder. text_carNum = (TextView) convertView.findViewById(R.id.park_carnum);
            viewHolder. text_parkNum = (TextView) convertView.findViewById(R.id.park_No);
            convertView.setTag(viewHolder);
        }else {
           viewHolder= (MyViewHolder) convertView.getTag();
        }
        viewHolder.text_carType.setText(parkBeanList.get(position).getCarType());

        if (parkBeanList.get(position).getParkType().equals("1")) {
            viewHolder.text_carType.setVisibility(View.INVISIBLE);
            viewHolder.img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.kongxinchewei));
            viewHolder.text_carNum.setText("空");
            viewHolder.text_carNum.setTextColor(context.getResources().getColor(R.color.chenull));
        } else if (parkBeanList.get(position).getParkType().equals("2")) {
            viewHolder.text_carNum.setText("有车");
            viewHolder.text_carNum.setTextColor(context.getResources().getColor(R.color.youche));
            viewHolder.img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.youcheliang));
            viewHolder.text_carType.setVisibility(View.INVISIBLE);
        } else if (parkBeanList.get(position).getParkType().equals("3")) {
            viewHolder.img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.qianfei));
            viewHolder.text_carNum.setText(parkBeanList.get(position).getCarNum());
            viewHolder.text_carType.setVisibility(View.VISIBLE);
            viewHolder.text_carNum.setVisibility(View.VISIBLE);

            viewHolder.text_carNum.setTextColor(context.getResources().getColor(R.color.park_blue));
            if (parkBeanList.get(position).getCarType().equals("货车")) {
                Resources resources = context.getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.shape_login_bigcar);
                viewHolder.text_carType.setBackground(drawable1);
            } else {
                Resources resources = context.getResources();
                Drawable drawable2 = resources.getDrawable(R.drawable.shape_login_smallcar);
                viewHolder.text_carType.setBackground(drawable2);
            }
        } else if (parkBeanList.get(position).getParkType().equals("4")) {
            viewHolder.img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.boxunpark));
            viewHolder.text_carNum.setText(parkBeanList.get(position).getCarNum());
            viewHolder.text_carType.setVisibility(View.VISIBLE);
            viewHolder.text_carNum.setVisibility(View.VISIBLE);
            viewHolder.text_carNum.setTextColor(context.getResources().getColor(R.color.park_blue));
            if (parkBeanList.get(position).getCarType().equals("货车")) {
                Resources resources = context.getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.shape_login_bigcar);
                viewHolder.text_carType.setBackground(drawable1);
            } else {
                Resources resources = context.getResources();
                Drawable drawable2 = resources.getDrawable(R.drawable.shape_login_smallcar);
                viewHolder.text_carType.setBackground(drawable2);
            }
        }

        viewHolder.text_parkNum.setText(parkBeanList.get(position).getParkNum());
        return convertView;
    }

    class MyViewHolder  {
        RelativeLayout item_mypark_btn;
        TextView text_carType;
        ImageView img_parkType;
        TextView text_carNum;
        TextView text_parkNum;

    }

}
