package com.jetcloud.hgbw.app;

/**
 * Created by Cqing on 2016/12/20.
 */

public class HgbwUrl {

    public final static String BASE_URL = "http://www.suiedai.com";
    public final static String HOME_URL = "http://192.168.3.28:9000";
//    public final static String BASE_URL = "http://192.168.3.7";
    public final static String TRADE_BASE_URL = "http://120.76.137.157:8089";
    /**
     * get
     * */
    public final static String HOME_DATA_URL = BASE_URL + "/getindexhtml.do";
    public final static String MACHINE_DATA_URL = HOME_URL + "/post/mechine";
    public final static String FOOD_BY_MACHINE_URL = HOME_URL + "/foodByMechine";
    public final static String VERIFICATION_URL = HOME_URL + "/common/phone";
    public final static String REGISTER_URL = HOME_URL + "/user/register";
    public final static String RESET_PWD_URL = HOME_URL + "/user/resetpwd";
    public final static String LOGIN_URL = HOME_URL + "/user/login";

    /**
     * post
     * */
    public final static String TAKE_FOOD = BASE_URL + "/order/takefood";
    public final static String MY_ORDER = BASE_URL + "/order";
    public final static String FOOD_DETAIL = BASE_URL + "/order/pdetail";
    public final static String TRADE_BIND = TRADE_BASE_URL + "/gateway/bind_profile";
    public final static String TRADE_PAY = TRADE_BASE_URL + "/gateway/pay";
    public final static String LOGOUT_URL = HOME_URL + "/user/logout";

    /***
     * Put
     * */
    public final static String TRADE_UNBIND = TRADE_BASE_URL + "/gateway/bind_profile";


    private HgbwUrl(){}
}
