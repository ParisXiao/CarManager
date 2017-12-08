package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class ChargeBean {
    private String CarNumber;
    private String StopTime;
    private String ParkPrice;
    public ChargeBean(String CarNumber,String StopTime,String ParkPrice){
        this.CarNumber=CarNumber;
        this.StopTime=StopTime;
        this.ParkPrice=ParkPrice;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }

    public String getParkPrice() {
        return ParkPrice;
    }

    public void setParkPrice(String parkPrice) {
        ParkPrice = parkPrice;
    }
}
