package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class PayInfoBean {
    private String TotalMoney;
    private String QFMoney;
    private String CurMoney;
    private String YHMoney;
    private String CarNum;
    private String StartTime;
    private String StopTime;

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getQFMoney() {
        return QFMoney;
    }

    public void setQFMoney(String QFMoney) {
        this.QFMoney = QFMoney;
    }

    public String getCurMoney() {
        return CurMoney;
    }

    public void setCurMoney(String curMoney) {
        CurMoney = curMoney;
    }

    public String getYHMoney() {
        return YHMoney;
    }

    public void setYHMoney(String YHMoney) {
        this.YHMoney = YHMoney;
    }

    public String getCarNum() {
        return CarNum;
    }

    public void setCarNum(String carNum) {
        CarNum = carNum;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }
}
