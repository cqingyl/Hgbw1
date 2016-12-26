package com.example.xutilstest;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Cqing on 2016/12/8.
 */

public class XUtilsTestApplication extends Application{
    public String s;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        s = "im in application";
    }
}
