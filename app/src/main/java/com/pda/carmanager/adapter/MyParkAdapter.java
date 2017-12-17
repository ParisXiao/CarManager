package com.pda.carmanager.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.inter.ParkItemOnInter;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private ParkItemOnInter parkItemOnInter;
    private List<MyParkBean> parkBeanList = new ArrayList<>();


    public MyParkAdapter(Context context, List<MyParkBean> parkBeanList, ParkItemOnInter parkItemOnInter) {
        this.context = context;
        this.parkBeanList = parkBeanList;
        this.parkItemOnInter=parkItemOnInter;
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
        View view = mInflater.inflate(R.layout.item_mypark, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).text_carType.setText(parkBeanList.get(position).getCarType());

        if (parkBeanList.get(position).getParkType().equals("1")) {
            ((MyViewHolder) holder).text_carType.setVisibility(View.INVISIBLE);
            ((MyViewHolder) holder).img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.kongxinchewei));
            ((MyViewHolder) holder).text_carNum.setText("空");
            ((MyViewHolder) holder).text_carNum.setTextColor(context.getResources().getColor(R.color.youche));
        } else if (parkBeanList.get(position).getParkType().equals("2")) {
            ((MyViewHolder) holder).text_carNum.setText("有车");
            ((MyViewHolder) holder).text_carNum.setTextColor(context.getResources().getColor(R.color.youche));
            ((MyViewHolder) holder).img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.youcheliang));
            ((MyViewHolder) holder).text_carType.setVisibility(View.INVISIBLE);
        } else if (parkBeanList.get(position).getParkType().equals("3")) {
            ((MyViewHolder) holder).img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.qianfei));
            ((MyViewHolder) holder).text_carNum.setText(parkBeanList.get(position).getCarNum());
            ((MyViewHolder) holder).text_carType.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).text_carNum.setVisibility(View.VISIBLE);

            ((MyViewHolder) holder).text_carNum.setTextColor(context.getResources().getColor(R.color.park_blue));
            if (parkBeanList.get(position).getCarType().equals("货车")) {
                Resources resources = context.getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.shape_login_bigcar);
                ((MyViewHolder) holder).text_carType.setBackground(drawable1);
            } else {
                Resources resources = context.getResources();
                Drawable drawable2 = resources.getDrawable(R.drawable.shape_login_smallcar);
                ((MyViewHolder) holder).text_carType.setBackground(drawable2);
            }
        } else if (parkBeanList.get(position).getParkType().equals("4")) {
            ((MyViewHolder) holder).img_parkType.setImageDrawable(context.getResources().getDrawable(R.drawable.boxunpark));
            ((MyViewHolder) holder).text_carNum.setText(parkBeanList.get(position).getCarNum());
            ((MyViewHolder) holder).text_carType.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).text_carNum.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).text_carNum.setTextColor(context.getResources().getColor(R.color.park_blue));
            if (parkBeanList.get(position).getCarType().equals("货车")) {
                Resources resources = context.getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.shape_login_bigcar);
                ((MyViewHolder) holder).text_carType.setBackground(drawable1);
            } else {
                Resources resources = context.getResources();
                Drawable drawable2 = resources.getDrawable(R.drawable.shape_login_smallcar);
                ((MyViewHolder) holder).text_carType.setBackground(drawable2);
            }
        }

        ((MyViewHolder) holder).text_parkNum.setText(parkBeanList.get(position).getParkNum());
        ((MyViewHolder) holder).item_mypark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (parkBeanList.get(position).getParkType()){
                    case "2":
                        parkItemOnInter.writeCarNum(parkBeanList.get(position).getParkingrecordid());
                        break;
                    case "3":
                        parkItemOnInter.payCar(parkBeanList.get(position).getParkingrecordid());
                        break;
                    case "4":
                        parkItemOnInter.AutoPayCar(parkBeanList.get(position).getCarNum(),parkBeanList.get(position).getParkingrecordid());
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_mypark_btn;
        TextView text_carType;
        ImageView img_parkType;
        TextView text_carNum;
        TextView text_parkNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_mypark_btn = (RelativeLayout) itemView.findViewById(R.id.item_mypark_btn);
            text_carType = (TextView) itemView.findViewById(R.id.park_carType);
            img_parkType = (ImageView) itemView.findViewById(R.id.img_park);
            text_carNum = (TextView) itemView.findViewById(R.id.park_carnum);
            text_parkNum = (TextView) itemView.findViewById(R.id.park_No);
        }
    }

}
