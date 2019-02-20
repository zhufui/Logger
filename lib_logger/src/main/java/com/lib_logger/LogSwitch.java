package com.lib_logger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 日志开关状态
 */
public class LogSwitch {
    public static final String CLOG = "clog";
    public static final String DLOG = "dlog";
    public static final String SP_NAME = "logstatus";

    public static void setClog(Context context, boolean clog) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(CLOG, clog);
        edit.apply();
    }

    public static void setDlog(Context context, boolean dlog) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(DLOG, dlog);
        edit.apply();
    }

    public static boolean getClog(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(CLOG, false);
    }

    public static boolean getDlog(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(DLOG, false);
    }

}
