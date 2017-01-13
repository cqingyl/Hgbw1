package com.jetcloud.hgbw.activity.demo;

import java.io.Serializable;

/**
 * Created by Cqing on 2017/1/11.
 */

public class BDMapData implements Serializable{
    private String name;
    private double latitude;//纬度
    private double longitude;//经度

    public BDMapData(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
