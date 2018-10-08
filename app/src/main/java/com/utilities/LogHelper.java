package com.utilities;


import android.util.Log;

/**
 * Created by Saferoad-Dev1 on 9/14/2017.
 */

public class LogHelper {

    public static void LOG_D(String tag, String msg){
        Log.d(tag, msg);
    }

    public static void LOG_V(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void LOG_E(String tag, String msg){
        Log.e(tag, msg);
    }

    public static void LOG_I(String tag, String msg){
        Log.i(tag, msg);
    }

    public static void LOG_W(String tag, String msg){
        Log.w(tag, msg);
    }

    public static void LOG_WTF(String tag, String msg) {
        Log.wtf(tag, msg);
    }

}
