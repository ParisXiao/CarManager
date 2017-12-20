package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class ChargeBean {
    private String Id;
    private String CarNumber;
    private String StopTime;
    private String StartTime;
    private String ParkPrice;
    private String Status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
