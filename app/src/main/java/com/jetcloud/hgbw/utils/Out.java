package com.jetcloud.hgbw.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * 日志打印
 * 
 * 
 */
public class Out {
	public static void outHbc(String log) {
		Log.e("hbc", log);
	}
	
	public static void Toast(Context context, String nr) {
		try {
			android.widget.Toast t = android.widget.Toast.makeText(context, nr,
					android.widget.Toast.LENGTH_SHORT);
			t.show();
		} catch (Exception e) {

		}
	}

	public static void ToastLong(Context context, String nr) {
		try {
			android.widget.Toast t = android.widget.Toast.makeText(context, nr,
					android.widget.Toast.LENGTH_LONG);
			t.show();
		} catch (Exception e) {

		}
	}

	public static boolean isActivityClosed(Context context) {
		return ((Activity) context).isFinishing();
	}
}
