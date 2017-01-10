package com.jetcloud.hgbw.activity.demo;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Cqing on 2017/1/10.
 */

public class BaiduLocation {
    /**
     * 经度
     *
     */
    public static double mylongitude = -1.0;
    /**
     * 纬度
     *
     */
    public static double mylatitude = -1.0;

    /**
     * 城市
     */
    public static String myCity = null;

    /**
     * 街道
     */
    public static String myCityadd = null;

    /**
     * 回调经纬度方法
     *
     * @param myLocationListener
     */
    public static void setMyLocationListener(MyLocationListener myLocationListener) {
        BaiduLocation.myLocationListener = myLocationListener;
    }

    public static MyLocationListener myLocationListener;

    /**
     * 回调经纬度的接口定义
     *
     * @author Administrator
     *
     */
    public static interface MyLocationListener {
        public void myLocation(double mylongitude, double mylatitude, String city, String street);
    };

    /**
     * 获取当前经纬度
     *
     * @param context
     */
    public static void getLocation(Context context) {

        final LocationClient locationClient = new LocationClient(context);

        // 设置定位条件

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("zhongchengbus"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(5000); // 设置定时定位的时间间隔。单位毫秒
        option.setAddrType("all");// 显示所有信息，街道
        locationClient.setLocOption(option);
        // 注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                Log.d("baidulocation", "baidulocation");
                // sb.append("Time : ");
                // sb.append(location.getTime());
                // sb.append("\nError code : ");
                // sb.append(location.getLocType());
                // sb.append("\nLatitude : ");
                // sb.append(location.getLatitude());
                // sb.append("\nLontitude : ");
                // sb.append(location.getLongitude());
                // sb.append("\nRadius : ");
                // sb.append(location.getRadius());
                // if (location.getLocType() == BDLocation.TypeGpsLocation){
                // sb.append("\nSpeed : ");
                // sb.append(location.getSpeed());
                // sb.append("\nSatellite : ");
                // sb.append(location.getSatelliteNumber());
                // } else
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // sb.append("\nAddress : ");
                    // sb.append(location.getAddrStr());
                    String district = location.getAddress().district;
                    myCityadd = location.getAddrStr();
                    myCity = location.getCity();
                    mylongitude = location.getLongitude();
                    mylatitude = location.getLatitude();
                    Log.d("baidulocation", mylongitude+"\n"+mylatitude+"\n"+ myCity+"\n"+district);
                    // 经纬度
                    if (myLocationListener != null) {
                        myLocationListener.myLocation(mylongitude, mylatitude, myCity, district);
                        locationClient.stop();
                    }
                }
            }

        });

        locationClient.start();
        /*
         * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
         * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
         * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
         * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
         */
        locationClient.requestLocation();
        Log.d("baidulocation", "baidulocation");
    }
}
