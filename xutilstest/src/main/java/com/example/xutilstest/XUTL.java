package com.example.xutilstest;

import android.content.Context;
import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by Cqing on 2016/12/8.
 */

public class XUTL {

    static Context context;
    private static final String dataBaseName = "hgbw.db";
    static DbManager.DaoConfig daoConfig;

    public static void setContext(Context context) {
        XUTL.context = context;
    }
    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡私有Cache目录的路径 或者
    public static String getSDCardPrivateCacheDir(Context context) {
            return isSDCardMounted() ? context.getExternalFilesDir(dataBaseName).getAbsolutePath() : context.getFilesDir().getAbsolutePath();
    }

    public static DbManager.DaoConfig getDaoConfig(){
        File file=new File(getSDCardPrivateCacheDir(context));
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

}
