package com.pda.carmanager.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class PrintBean  implements Serializable{
    private String carNo;
    private String startTime;
    private String MemberNo;
    private String Url;
    private String carNum;
    private List<IsQFModel> isQFModels;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMemberNo() {
        return MemberNo;
    }

    public void setMemberNo(String memberNo) {
        MemberNo = memberNo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public List<IsQFModel> getIsQFModels() {
        return isQFModels;
    }

    public void setIsQFModels(List<IsQFModel> isQFModels) {
        this.isQFModels = isQFModels;
    }

    public static class IsQFModel implements Serializable{
        private String JD;
        private String startTime;
        private String stopTime;
        private String carNO;
        private String money;

        public String getJD() {
            return JD;
        }

        public void setJD(String JD) {
            this.JD = JD;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStopTime() {
            return stopTime;
        }

        public void setStopTime(String stopTime) {
            this.stopTime = stopTime;
        }

        public String getCarNO() {
            return carNO;
        }

        public void setCarNO(String carNO) {
            this.carNO = carNO;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
