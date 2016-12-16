package com.jetcloud.hgbw.utils;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jetcolud.hgbw.R;

/**
 * Created by Cqing on 2016/12/13.
 */

public class Util {
    private Util(){}

    public static boolean isCornerExits(Activity context) {
        int shopCarNumber = SharedPreferenceUtils.getShopCarNumber();
        TextView tvCorner;
        tvCorner = (TextView) context.findViewById(R.id.tv_corner);
        RelativeLayout layout = (RelativeLayout) context.findViewById(R.id.rl_shop_car);
        if (shopCarNumber > 0){
            tvCorner.setText(String.valueOf(SharedPreferenceUtils.getShopCarNumber()));
            return true;
        }else{
            layout.setVisibility(View.GONE);
            return false;
        }
    }
    public static void addCornerNum(Activity context) {
        int shopCarNumber = SharedPreferenceUtils.getShopCarNumber();
         SharedPreferenceUtils.setShopCarNumber(++shopCarNumber);
    }
}
