package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkBean {
    private String CarType;
    private String ParkType;
    private String CarNum;
    private String ParkNum;
    private String parkingrecordid;
    private String startTime;
    private boolean in=false;
    private boolean out=false;

    public MyParkBean() {
    }

    public MyParkBean(String CarType, String ParkType, String CarNum, String ParkNum){
        this.CarType=CarType;
        this.ParkType=ParkType;
        this.CarNum=CarNum;
        this.ParkNum=ParkNum;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getParkingrecordid() {
        return parkingrecordid;
    }

    public void setParkingrecordid(String parkingrecordid) {
        this.parkingrecordid = parkingrecordid;
    }
}
