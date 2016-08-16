package com.wanghaisheng.template_lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wanghaisheng.template_lib.BaseApplication;

/**
 * Created by sheng on 2016/5/21.
 * 系统工具类：获取android系统信息
 */
public class SystemUtils {

    public static String getAndroidId() {
        Context context = BaseApplication.context().getApplicationContext();
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceId() {
        Context context = BaseApplication.context().getApplicationContext();
        String deviceId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() == null) {
            deviceId = getAndroidId();
        } else {
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }

    //获取当前版本号
    public static String getAppVersionName() {
        String versionName = "";
        try {
            Context context = BaseApplication.context().getApplicationContext();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
