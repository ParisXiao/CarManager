package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class MsgBean {
    String id;
    String msg_time;
    String msg_title;
    String msg_titleColor;
    String msg_content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public String getMsg_titleColor() {
        return msg_titleColor;
    }

    public void setMsg_titleColor(String msg_titleColor) {
        this.msg_titleColor = msg_titleColor;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }
}
