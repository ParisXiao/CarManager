package com.pda.carmanager.bean;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class MsgBean {
    String msg_day;
    String msg_hour;
    String msg_title;
    String msg_content;
    public MsgBean( String msg_day,   String msg_hour, String msg_title,String msg_content){
        this.msg_day=msg_day;
        this.msg_hour=msg_hour;
        this.msg_title=msg_title;
        this.msg_content=msg_content;
    }

    public String getMsg_day() {
        return msg_day;
    }

    public void setMsg_day(String msg_day) {
        this.msg_day = msg_day;
    }

    public String getMsg_hour() {
        return msg_hour;
    }

    public void setMsg_hour(String msg_hour) {
        this.msg_hour = msg_hour;
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
