package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class DakaBean {
    private String DakaTime;
    private String DakaAddress;
    public DakaBean(String DakaTime,String DakaAddress){
        this.DakaTime=DakaTime;
        this.DakaAddress=DakaAddress;
    }

    public String getDakaTime() {
        return DakaTime;
    }

    public void setDakaTime(String dakaTime) {
        DakaTime = dakaTime;
    }

    public String getDakaAddress() {
        return DakaAddress;
    }

    public void setDakaAddress(String dakaAddress) {
        DakaAddress = dakaAddress;
    }
}
