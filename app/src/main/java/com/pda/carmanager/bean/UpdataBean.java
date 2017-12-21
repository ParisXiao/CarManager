package com.pda.carmanager.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/12/21.
 */

public class UpdataBean  implements Serializable{
    private String appVersion;
    private String updataUrl;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUpdataUrl() {
        return updataUrl;
    }

    public void setUpdataUrl(String updataUrl) {
        this.updataUrl = updataUrl;
    }
}
