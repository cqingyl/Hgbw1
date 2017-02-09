package com.jetcloud.hgbw.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.view.CusAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * Created by Cqing
 * on 2017/2/7.
 * *****************************************************
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 * *****************************************************
 */


public class JumpUtils {
    public static void check405 (final Context context, String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.has("code")){
            if (jsonObject.getString("code").equals("409")){
                SharedPreferenceUtils.setIdentity(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setMyAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setMyPassword(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
                SharedPreferenceUtils.setTradeAccount(SharedPreferenceUtils.UNBINDING_STATE);
                final CusAlertDialog cusAlertDialog = new CusAlertDialog(context);
                cusAlertDialog.setTitle("提示");
                cusAlertDialog.setContent("你的账号已在其他地方登录");
                cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cusAlertDialog.dismiss();
                    }
                });
                cusAlertDialog.show();
            } else if (jsonObject.getString("code").equals("405")){
                SharedPreferenceUtils.setIdentity(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setMyAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setMyPassword(SharedPreferenceUtils.WITHOUT_LOGIN);
                SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
                SharedPreferenceUtils.setTradeAccount(SharedPreferenceUtils.UNBINDING_STATE);
                final CusAlertDialog cusAlertDialog = new CusAlertDialog(context);
                cusAlertDialog.setTitle("提示");
                cusAlertDialog.setContent("登录失效请重新登录");
                cusAlertDialog.canceledOnTouchOutside();
                cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cusAlertDialog.dismiss();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
                cusAlertDialog.show();
            }
        }
    }
}
