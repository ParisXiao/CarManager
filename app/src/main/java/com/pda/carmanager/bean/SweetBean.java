package com.pda.carmanager.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class SweetBean {
    private String id;
    private String name;
    private List<SweetDuanBean> sweetDuanBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SweetDuanBean> getSweetDuanBean() {
        return sweetDuanBean;
    }

    public void setSweetDuanBean(List<SweetDuanBean> sweetDuanBean) {
        this.sweetDuanBean = sweetDuanBean;
    }

    @Override
    public String toString() {
        return "SweetBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sweetDuanBean=" + sweetDuanBean +
                '}';
    }
}
