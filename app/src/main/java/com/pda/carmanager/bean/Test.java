package com.pda.carmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class Test implements Serializable{
    private String id;
    private String name;
    public Test(String id,String name){
        this.id=id;
        this.name=name;
    }

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

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
