package com.pda.carmanager.config;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class UrlConfig {
    /**
     * 接口配置
     */
//    public static String HttpIpPost="http://www.bianlile.cc:89/" ;
    public static String HttpIpPost="http://192.168.43.249:8001/" ;
    public static String LoginPost=HttpIpPost+"api/Login/CheckLogin";//登录
    public static String LogoutPost=HttpIpPost+"api/Login/OutLogin";//登出
    public static String ParkPost=HttpIpPost+"api/CarManage/SelParkingLotByStatus";//车位列表
    public static String DakaPost=HttpIpPost+"api/CarManage/AddKQ";//打卡
    public static String SweetPost=HttpIpPost+"api/CarManage/SelJDByCompany";//街道
    public static String SweetDuanPost=HttpIpPost+"api/CarManage/SelJDDByJD";//街道段
    public static String AddParkPost=HttpIpPost+"api/CarManage/AddParkingLot";//添加车位
    public static String PostParkPost=HttpIpPost+"api/CarManage/EnregisterCarNum";//登记车位信息
    public static String PayInfoPost=HttpIpPost+"api/CarManage/PreMoney";//缴费信息
    public static String PayPost=HttpIpPost+"api/CarManage/OKMoney";//支付
    public static String GetDakaPost=HttpIpPost+"api/CarManage/SelKQ";//查询打卡记录
    public static String ChargePost=HttpIpPost+"api/CarManage/SelParkingRecord";//查询打卡记录
    public static String PrintPost=HttpIpPost+"api/CarManage/EnregisterCarNumPrint";//查询打印信息

    public static String SMSUrl="http://api.boxunpark.com/SMS/sendMsg";//短信验证接口
}
