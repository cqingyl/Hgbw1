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
    private final static String MY_PASSWORD = "my_account";
    private final static String TRADE_ACCOUNT = "trade_account";
    private final static String IDENTITY = "identity";
    public final static String BINDING_STATE = "bind";
    public final static String UNBINDING_STATE = "unbind";
    public final static String MACHINE_NUM = "machine_num";
    public final static String WITHOUT_LOGIN = "without_login";
    public final static String WITHOUT_LOCATION = "without_location";

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
        return preferences.getString(MY_ACCOUNT, WITHOUT_LOGIN);
    }

    public static void setMyAccount(String phoneNum) {
        editor.putString(MY_ACCOUNT, phoneNum).commit();
    }

    public static String getBindStatus() {
        return preferences.getString(BINDING_STATE, UNBINDING_STATE);
    }

    public static void setBindStatus(String bindStatus) {
        editor.putString(BINDING_STATE, bindStatus).commit();
    }

    public static String getTradeAccount() {
        return preferences.getString(TRADE_ACCOUNT, WITHOUT_LOGIN);
    }

    public static void setTradeAccount(String tradePhoneNum) {
        editor.putString(TRADE_ACCOUNT, tradePhoneNum).commit();
    }

    public static String getIdentity() {
        return preferences.getString(IDENTITY, WITHOUT_LOGIN);
    }

    public static void setIdentity(String identity) {
        editor.putString(IDENTITY, identity).commit();
    }

    public static String getMachineNum() {
        return preferences.getString(MACHINE_NUM, WITHOUT_LOCATION);
    }

    public static void setMachineNum(String machineNum) {
        editor.putString(MACHINE_NUM, machineNum).commit();
    }

    public static String getMyPassword() {
        return preferences.getString(MY_PASSWORD, WITHOUT_LOGIN);
    }

    public static void setMyPassword(String myPassword) {
        editor.putString(MY_PASSWORD, myPassword).commit();
    }


    public static void clear() {
        editor.clear();
        editor.commit();
    }
}
