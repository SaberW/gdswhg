package com.force.librarybase.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * ToastUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param type 1:提示 2.成功 3警告  4错误
     * @param msg
     * @param isWithIcon 是否显示图标
     * @return
     */
    public static void showToast(Context context,int type,String msg,boolean isWithIcon){
        switch (type) {
            case 1:
                Toasty.info(context,msg, Toast.LENGTH_SHORT, isWithIcon).show();
                break;
            case 2:
                Toasty.success(context,msg, Toast.LENGTH_SHORT, isWithIcon).show();
                break;
            case 3:
                Toasty.warning(context,msg, Toast.LENGTH_SHORT, isWithIcon).show();
                break;
            case 4:
                Toasty.error(context,msg, Toast.LENGTH_SHORT, isWithIcon).show();
                break;
        }
    }

}
