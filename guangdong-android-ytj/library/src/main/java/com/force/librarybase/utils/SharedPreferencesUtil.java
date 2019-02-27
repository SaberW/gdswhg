package com.force.librarybase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.force.librarybase.BaseApplication;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @Package com.dayhr.cscec.pmc.common.utils
 * @Description:
 * @date 16/8/1 下午2:22
 */
public class SharedPreferencesUtil {
    private static SharedPreferences msp;
    private static SharedPreferences search;

    private static void lazyInit() {
        if (msp == null) {
            msp = BaseApplication.getInstance().getSharedPreferences("caterers_pos_new", Context.MODE_PRIVATE);
        }
    }

    public static int getInt(String key, int defValue) {
        lazyInit();
        return msp.getInt(key, defValue);
    }

    public static void saveInt(String key, int value) {
        lazyInit();
        msp.edit().putInt(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        lazyInit();
        return msp.getLong(key, defValue);
    }

    public static void saveLong(String key, long value) {
        lazyInit();
        msp.edit().putLong(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        lazyInit();
        return msp.getString(key, defValue);
    }

    public static void saveString(String key, String value) {
        lazyInit();
        msp.edit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        lazyInit();
        return msp.getBoolean(key, defValue);
    }

    public static void saveBoolean(String key, boolean value) {
        lazyInit();
        msp.edit().putBoolean(key, value).commit();
    }

    public static float getFloat(String key, float defValue) {
        lazyInit();
        return msp.getFloat(key, defValue);
    }

    public static void saveFloat(String key, float value) {
        lazyInit();
        msp.edit().putFloat(key, value).commit();
    }

    public static void clearAll() {
        lazyInit();
        msp.edit().clear().commit();
    }

    public static void clearByKey(String key) {
        lazyInit();
        msp.edit().remove(key).commit();
    }
}
