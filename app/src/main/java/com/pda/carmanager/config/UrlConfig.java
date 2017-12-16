package com.pda.carmanager.config;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class UrlConfig {
    /**
     * 接口配置
     */
    public static String HttpIpPost="http://192.168.1.111:8001/" ;
    public static String LoginPost=HttpIpPost+"api/Login/CheckLogin";//登录
    public static String LogoutPost=HttpIpPost+"api/Login/OutLogin";//登出
    public static String ParkPost=HttpIpPost+"api/CarManage/SelParkingLotByStatus";//车位列表
    public static String DakaPost=HttpIpPost+"api/CarManage/AddKQ";//打卡
    public static String SweetPost=HttpIpPost+"api/CarManage/SelJDByCompany";//街道
    public static String SweetDuanPost=HttpIpPost+"api/CarManage/SelJDDByJD";//街道段
    public static String AddParkPost=HttpIpPost+"api/CarManage/AddParkingLot";//添加车位

    public static String SMSUrl="http://api.boxunpark.com/SMS/sendMsg";//短信验证接口
}
