package com.jetcloud.hgbw.app;

/**
 * Created by Cqing on 2016/12/20.
 */

public class HgbwUrl {

//    public final static String HOME_URL = "http://192.168.3.16:9000";
    public final static String HOME_URL = "http://120.25.159.143";
    /**
     * get
     * */
    public final static String MACHINE_DATA_URL = HOME_URL + "/post/mechine";
    public final static String FOOD_BY_MACHINE_URL = HOME_URL + "/foodByMechine";
    public final static String VERIFICATION_URL = HOME_URL + "/common/phone";
    public final static String MACHINE_LOCATION_URL = HOME_URL + "/mechine/bylocate";
    public final static String FOOD_DETAIL_URL = HOME_URL + "/food";
    public final static String BANNER_URL = HOME_URL + "/mechine/banner";
    public final static String GET_ORDER_URL = HOME_URL + "/user/order";

    /**
     * post
     * */
    public final static String TRADE_BIND = HOME_URL + "/user/tradebookbind";
    public final static String REGISTER_URL = HOME_URL + "/user/register";
    public final static String LOGIN_URL = HOME_URL + "/user/login";
    public final static String RESET_PWD_URL = HOME_URL + "/user/resetpwd";
    public final static String LOGOUT_URL = HOME_URL + "/user/logout";
    public final static String PIC_AND_NICK_URL = HOME_URL + "/user/info";//"pic","nickname"
    public final static String PAY_URL = HOME_URL + "/user/buyfood";

    /***
     * Put
     * */


    private HgbwUrl(){}
}
