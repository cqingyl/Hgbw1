package com.jetcloud.hgbw.utils;

import org.xutils.DbManager;

/**
 * Created by Cqing on 2016/12/13.
 */

public class XUtil {

    private XUtil() {
    }

    private static final String dataBaseName = "hgbw.db";
    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(){
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName(dataBaseName)
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
}
