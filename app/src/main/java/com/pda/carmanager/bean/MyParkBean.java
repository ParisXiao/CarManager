package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkBean {
    private String CarType;
    private String ParkType;
    private String CarNum;
    private String ParkNum;
    public MyParkBean( String CarType,String ParkType,String CarNum,String ParkNum){
        this.CarType=CarType;
        this.ParkType=ParkType;
        this.CarNum=CarNum;
        this.ParkNum=ParkNum;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public String getParkType() {
        return ParkType;
    }

    public void setParkType(String parkType) {
        ParkType = parkType;
    }

    public String getCarNum() {
        return CarNum;
    }

    public void setCarNum(String carNum) {
        CarNum = carNum;
    }

    public String getParkNum() {
        return ParkNum;
    }

    public void setParkNum(String parkNum) {
        ParkNum = parkNum;
    }
}
