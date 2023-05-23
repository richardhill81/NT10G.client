package com.shwangce.nt10g.client.util;

import com.shwangce.nt10g.client.BuildConfig;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public abstract class Log {
    public static boolean showDebug = BuildConfig.DEBUG;

    public static void d(String tag,String msg) {
        if(showDebug) {
            android.util.Log.d(tag,msg);
        }
    }

    public static void e(String tag,String msg) {
        //if(showDebug) {
            android.util.Log.e(tag,msg);
        //}
    }

    public static void e(String tag,String msg,Throwable tr) {
        //if(showDebug) {
            android.util.Log.e(tag,msg,tr);
        //}
    }

    public static void i(String tag,String msg) {
        //if(showDebug) {
            android.util.Log.i(tag,msg);
        //}
    }

    public static void i(String tag,String msg,Throwable tr) {
        //if(showDebug) {
            android.util.Log.i(tag,msg,tr);
        //}
    }

    public static void w(String tag,String msg) {
        //if(showDebug) {
            android.util.Log.w(tag,msg);
        //}
    }
}
