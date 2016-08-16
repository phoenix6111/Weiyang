package com.wanghaisheng.template_lib.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.wanghaisheng.template_lib.services.SaveAllImageService;

import java.util.ArrayList;

/**
 * Created by sheng on 2016/5/7.
 */
public class BaseNavigator {

    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context,cls);
        start(context,intent);
    }

    public static void start(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context,cls);
        intent.putExtras(bundle);

        start(context,intent);
    }

    /**
     * 设置转场动画的bundle
     * @param activity
     * @param intent
     * @param bundle
     */
    public static void start(Activity activity, Intent intent, Bundle bundle) {
//        LogUtils.d(activity);
        ActivityCompat.startActivity(activity, intent, bundle);
    }

    /**
     * 打开下载图片的service
     * @param context
     * @param urls
     */
    public static void startDownloadImageService(Context context, ArrayList<String> urls) {
        Intent saveIntent = new Intent(context,SaveAllImageService.class);
        saveIntent.putStringArrayListExtra(SaveAllImageService.ARG_URLS, urls);
        context.startService(saveIntent);
    }

}
