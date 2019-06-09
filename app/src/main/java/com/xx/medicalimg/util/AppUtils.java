package com.xx.medicalimg.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 软件生命周期中常用工具
 */
public class AppUtils {
	public static void toast(Context context, String str){
		Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
	}
}
