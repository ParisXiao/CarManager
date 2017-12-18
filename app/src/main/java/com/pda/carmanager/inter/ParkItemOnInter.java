package com.pda.carmanager.inter;


/**
 * Created by Administrator on 2017/12/10 0010.
 */

public interface ParkItemOnInter {
    void writeCarNum(String Id);
    void payCar(String Id);
    void AutoPayCar(String carNum,String Id);
    void LongOnItem(boolean print,String Id);
}
