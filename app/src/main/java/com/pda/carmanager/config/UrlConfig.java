package com.pda.carmanager.config;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class UrlConfig {
    /**
     * 接口配置
     */
    public static String HttpIpPost="http://192.168.1.238:8008/" ;
    public static String LoginPost=HttpIpPost+"api/Login/CheckLogin";//登录
    public static String LogoutPost=HttpIpPost+"api/Login/OutLogin";//登出
    public static String ParkPost=HttpIpPost+"api/CarManage/SelParkingLotByStatus";//车位列表
    public static String DakaPost=HttpIpPost+"api/CarManage/AddKQ";//打卡

    public static String SMSUrl="http://api.boxunpark.com/SMS/sendMsg";//短信验证接口
}
