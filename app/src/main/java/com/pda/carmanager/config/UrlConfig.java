package com.pda.carmanager.config;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class UrlConfig {
    /**
     * 接口配置
     */
    public static String HttpIpPost="http://192.168.43.249:8008/" ;
    public static String LoginPost=HttpIpPost+"api/Login/CheckLogin";
    public static String LogoutPost=HttpIpPost+"api/Login/OutLogin";

    public static String SMSUrl="http://api.boxunpark.com/SMS/sendMsg";//短信验证接口
}
