package com.example.xutilstest;

import android.content.Context;

import com.google.gson.Gson;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by Cqing on 2016/12/8.
 */

public class XUTL {

    private static final String dataBaseName = "hgbw.db";
    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(Context context){
        File file = new File(context.getDir("database", 0).getAbsolutePath());
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName(dataBaseName)
                    .setDbDir(file)
                    .setDbVersion(2)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }
    static Gson gson = new Gson();


    public static <T> T json2Obj(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
