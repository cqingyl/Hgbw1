package com.jetcloud.hgbw.utils;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jetcolud.hgbw.R;

/**
 * Created by Cqing on 2016/12/22.
 */

public class ShopCarUtil {
    private ShopCarUtil(){}

    //改变购物车角标是否显示
    public static void ChangeCorner(Activity context, int total) {
        RelativeLayout rl_shop_car = (RelativeLayout) context.findViewById(R.id.rl_shop_car);
        TextView tvCorner = (TextView) context.findViewById(R.id.tv_corner);
        if (total <= 0) {
            rl_shop_car.setVisibility(View.GONE);
        } else {
            rl_shop_car.setVisibility(View.VISIBLE);
            tvCorner.setText(String.valueOf(total));
        }
    }
    //增加购物车数量
//    public static void addCornerNum(Activity context) {
//        int shopCarNumber = SharedPreferenceUtils.getShopCarNumber();
//        SharedPreferenceUtils.setShopCarNumber(++shopCarNumber);
//        ChangeCorner(context);
//    }
    //减少购物车数量
//    public static void decCornerNum(Activity context, int goodNum) {
//        int shopCarNumber = SharedPreferenceUtils.getShopCarNumber();
//        if (shopCarNumber > 0) {
//            shopCarNumber -= goodNum;
//            Log.i(TAG, "decCornerNum: " + shopCarNumber);
//            if (shopCarNumber <= 0) {
//                shopCarNumber = 0;
//            }
//        }
//        SharedPreferenceUtils.setShopCarNumber(shopCarNumber);
//        ChangeCorner(context);
//    }
}
