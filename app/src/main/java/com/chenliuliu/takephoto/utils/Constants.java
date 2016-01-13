package com.chenliuliu.takephoto.utils;


public class Constants {
    public static final String APP_ID = "wx602c36584680e858";//微信
    public static final String APP_APPID = "fdff3bd52e93a63fea6bfb812fdc31e5";
    public static final String APP_APPSECRET = "FOYFhGtTcqFCekByHUoSkbygNjEII98LcSNsZTgexh1";
    public static final String Local_DATE = "localDate";

    public static final class Shrae {
        public static final String isFirst = "isFirst";//是否第一次登录
        public static final String isLogin = "isLogin";//是否已经登录
        public static final String isWifi = "isWifi";//是否wifi提醒
        public static final String isMessage = "isMessage";//是否接收消息
        public static final String nonce = "nonce";//token
        public static final String userInfo = "userinfo";//用户信息bean
        public static final String psd = "psd";//密码
        public static final String id = "id";//用户id
    }

    public static final class OrderType {
        public static final String ordreType = "ordretype";//订单类型
        public static final int WAIT_SPRAY = 0x234;//等喷洒
        public static final int WAIT_JS = 0x345;//等结算
        public static final int FINISHED = 0x456;//已完成
    }

    public static final class RequestCode {
        public static final int code_equipment = 0x934;//设备
        public static final int code_id = 0x8453;//身份证
        public static final int code_file = 0x745;//从相册选取
        public static final int code_camera = 0x6745;//拍照选取
        public static final int photo_zoom = 0x57452;//裁剪
        public static final int code_invoice = 0x4343;//设备照片
        public static final int code_account = 0x234323;//账户
        public static final int code_province = 0x24323;//省选择
        public static final int code_city = 0x124323;//市选择
    }

    public static final class ModifyType {
        public static final int phone = 0x234;
        public static final int name = 0x2934;
        public static final int sex = 0x2344;
        public static final int identify = 0x224;
        public static final int province = 0x23234;
        public static final int city = 0x293234;

    }

    public static final class OrderOperatType {
        public static final int finish = 0x2324;//已完成订单
        public static final int cancle = 0x2934;//取消订单
        public static final int modify = 0x2344;//修改
        public static final int Js = 0x23444;//结算订单
        public static final int sucess = 0x234;
        public static final int sucess_take = 0x2334;
        public static final int logout = 0x2834;//退出登录
        public static final int refresh = 0x9834;//收到推送，更新订单
    }

    public static final class EventType {
        public static final int gotowaitspary = 0x234;//跳转等喷洒页面
        public static final int gotowaitjs = 0x2934;//跳转等结算页面
        public static final int gotofinish = 0x29324;//跳转已完成页面
    }

}
