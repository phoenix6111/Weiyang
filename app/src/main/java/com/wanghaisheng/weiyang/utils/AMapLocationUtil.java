package com.wanghaisheng.weiyang.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Author: sheng on 2016/8/17 11:14
 * Email: 1392100700@qq.com
 */
public class AMapLocationUtil implements AMapLocationListener{

    private Context mContext;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private MyLocationListener mLocationListener;

    public void setmLocationListener(MyLocationListener mLocationListener) {
        this.mLocationListener = mLocationListener;
    }

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    public AMapLocationUtil(Context mContext,MyLocationListener myLocationListener) {
        this.mContext = mContext;
        this.mLocationListener = myLocationListener;

        initAMapLocationClient();
    }

    public void initAMapLocationClient() {
        mLocationClient = new AMapLocationClient(mContext);

        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        //高德定位服务包含GPS和网络定位（Wi-Fi和基站定位）两种能力。定位SDK将GPS、网络定位能力进行了封装，以三种定位模式对外开放，SDK默认选择使用高精度定位模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void startLocation() {
        if(mLocationClient == null) {
            initAMapLocationClient();
        }

        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(mLocationListener != null) {
            mLocationListener.myLocation(aMapLocation);
        }
    }

    public interface MyLocationListener {
        void myLocation(AMapLocation mapLocation);
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        this.mContext = null;
    }
}
