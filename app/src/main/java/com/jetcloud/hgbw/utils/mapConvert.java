package com.jetcloud.hgbw.utils;

import com.jetcloud.hgbw.baidumap.BDMapData;

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
 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法
 * bd_encrypt 将 GCJ-02 坐标转换成 BD-09 坐标， bd_decrypt 反之
 */


public class mapConvert {
    private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static BDMapData bd_encrypt(double gcj_lat, double gcj_lon) {
        double x = gcj_lon, y = gcj_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        gcj_lon = z * Math.cos(theta) + 0.0065;
        gcj_lat = z * Math.sin(theta) + 0.006;
        return new BDMapData(gcj_lat, gcj_lon);
    }

    public static BDMapData bd_decrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        bd_lon = z * Math.cos(theta);
        bd_lat = z * Math.sin(theta);
        return new BDMapData(bd_lat, bd_lon);
    }
}
