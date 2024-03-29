package com.xx.medicalimg.util;

import android.util.Log;

/**
 * 打印日志工具类，e、w、i、d、v优先级递减
 */
public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = VERBOSE;
    private static final String TAG = "『医疗影像』";
    public static void v(String tag,String msg){
        if(level <= VERBOSE){
            Log.v(TAG + tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if(level <= DEBUG){
            Log.d(TAG +tag,msg);
        }
    }public static void i(String tag,String msg){
        if(level <= INFO){
            Log.i(TAG +tag,msg);
        }
    }public static void w(String tag,String msg){
        if(level <= WARN){
            Log.w(TAG +tag,msg);
        }
    }public static void e(String tag,String msg){
        if(level <= ERROR){
            Log.e(TAG +tag,msg);
        }
    }

    /**
     * 停止打印日志
     */
   public  static void stopLogging(){
       LogUtils.level = NOTHING;
    }

}
