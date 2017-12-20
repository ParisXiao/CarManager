package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class ErrorBean {
    private String ErrorAddress;
    private String ErrorTime;
    private String ErrorStatus;

    public String getErrorStatus() {
        return ErrorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        ErrorStatus = errorStatus;
    }

    public String getErrorAddress() {
        return ErrorAddress;
    }

    public void setErrorAddress(String errorAddress) {
        ErrorAddress = errorAddress;
    }

    public String getErrorTime() {
        return ErrorTime;
    }

    public void setErrorTime(String errorTime) {
        ErrorTime = errorTime;
    }
}
