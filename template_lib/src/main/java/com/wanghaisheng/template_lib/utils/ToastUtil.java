package com.wanghaisheng.template_lib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanghaisheng.template_lib.BaseApplication;
import com.wanghaisheng.template_lib.R;

/**
 * Created by sheng on 2016/4/14.
 */
public class ToastUtil {
    private static String lastToast = "";
    private static long lastToastTime;

    public static void showToast(Context context,String message) {
        showToast(context,message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void showToast(Context context,int message) {
        showToast(context,message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void showSnackbar(String message, View root) {
        Snackbar snackbar = Snackbar.make(root,message,Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout)snackbar.getView();
        //Snackbar 透明度
        ve.setAlpha(0.95f);

        //Snackbar 文字颜色
        //((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public static void staticShowSnackbar(String message, View root) {
        Snackbar snackbar = Snackbar.make(root,message,Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout)snackbar.getView();
        //Snackbar 透明度
        ve.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        ve.setAlpha(0.7f);
        //Snackbar 文字颜色
        //((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public static void showToast(Context context,int message, int icon) {
        Context tContext = null;
        if(context == null) {
            tContext = BaseApplication.context();
        } else {
            tContext = context;
        }

        showToast(tContext,message, Toast.LENGTH_LONG, icon);
    }

    public static void showToast(Context context,int message, int duration, int icon) {
        Context tContext = null;
        if(context == null) {
            tContext = BaseApplication.context();
        } else {
            tContext = context;
        }
        showToast(tContext,message, duration, icon, Gravity.BOTTOM);
    }

    public static void showToast(Context context,int message, int duration, int icon,
                                 int gravity) {

        Context tContext = null;
        if(context == null) {
            tContext = BaseApplication.context();
        } else {
            tContext = context;
        }

        showToast(tContext,tContext.getString(message), duration, icon, gravity);
    }

    public static void showToast(Context context,int message, int duration, int icon,
                                 int gravity, Object... args) {
        Context tContext = null;
        if(context == null) {
            tContext = BaseApplication.context();
        } else {
            tContext = context;
        }

        showToast(tContext,tContext.getString(message, args), duration, icon, gravity);
    }

    public static void showCenterToast(Context context,int resId) {
        Context tContext = null;
        if(context == null) {
            tContext = BaseApplication.context();
        } else {
            tContext = context;
        }
        showCenterToast(tContext,tContext.getString(resId));
    }

    public static void showCenterToast(Context context,String message) {
        showToast(context,message, Toast.LENGTH_LONG,0, Gravity.CENTER);
    }

    public static void showShortCenterToast(Context context,String message) {
        showToast(context,message, Toast.LENGTH_SHORT,0, Gravity.CENTER);
    }

    public static void showToast(Context context,String message, int duration, int icon,
                          int gravity) {
        if(context == null) {
            context = BaseApplication.context();
        }

        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast)
                    || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(context).inflate(
                        R.layout.common_view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ImageView imageView = ((ImageView) view.findViewById(R.id.icon_iv));
                    imageView.setImageResource(icon);
                    imageView.setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(context);
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }

                toast.setDuration(duration);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }
}
