package com.jetcloud.hgbw.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Cqing on 2016/12/13.
 */

public class SharedPreferenceUtils {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static Context mContext;
    private final static String PACKAGE_NAME = "com.jetcloud.hgbw";
    private final static String SHOP_CAR_NUMBER = "shopCarNumber";
    private final static String MY_ACCOUNT = "my_account";
    private final static String TRADE_ACCOUNT = "trade_account";
    private final static String BIND_STATUS = "bind_status";
    public final static String BINDING_STATE = "binding_state";
    public final static String UNBINDING_STATE = "unbinding-State";

    public static void initData (Context context){
        mContext = context;
        preferences = context.getSharedPreferences(PACKAGE_NAME, Activity.MODE_PRIVATE);
        if (preferences != null){
            editor = preferences.edit();
        }
    }

    public static int getShopCarNumber() {
        return preferences.getInt(SHOP_CAR_NUMBER, 0);
    }

    public static void setShopCarNumber(int shopCarNumber) {
        editor.putInt(SHOP_CAR_NUMBER, shopCarNumber).commit();
    }

    public static String getMyAccount() {
        return preferences.getString(MY_ACCOUNT, "without login");
    }

    public static void setMyAccount(String phoneNum) {
        editor.putString(MY_ACCOUNT, phoneNum).commit();
    }

    public static String getBindStatus() {
        return preferences.getString(BIND_STATUS, UNBINDING_STATE);
    }

    public static void setBindStatus(String bindStatus) {
        editor.putString(BIND_STATUS, bindStatus).commit();
    }

    public static String getTradeAccount() {
        return preferences.getString(TRADE_ACCOUNT, "without login");
    }

    public static void setTradeAccount(String tradePhoneNum) {
        editor.putString(TRADE_ACCOUNT, tradePhoneNum).commit();
    }

    public static void clear() {
        editor.clear();
        editor.commit();
    }
}
