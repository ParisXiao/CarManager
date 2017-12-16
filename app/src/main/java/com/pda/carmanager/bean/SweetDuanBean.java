package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class SweetDuanBean {
    private String Id;
    private String Name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "SweetDuanBean{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
