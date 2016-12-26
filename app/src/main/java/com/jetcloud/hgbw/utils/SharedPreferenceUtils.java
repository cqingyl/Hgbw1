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
    private static String SHOP_CAR_NUMBER = "shopCarNumber";

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

    public static void clear() {
        editor.clear();
        editor.commit();
    }
}
