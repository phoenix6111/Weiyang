package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.R;

/**
 * Author: sheng on 2016/8/14 16:11
 * Email: 1392100700@qq.com
 */
public class MyLocationHandler implements LocationSource,
        AMapLocationListener {

    private Context mContext;
    private AMap mAMap;


    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private OnLocationChangedListener mLocationChangeListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    //方向监听传感器，更新定位图标的方向
    private MyOrientationListener mOrientationListener;
    //保存当前的方向传感器偏移量
    private float mCurrentX;

    //我的位置的 marker
    private Marker mMyLocationMarker;
    //当前定位的city
    private String mCity = "深圳";
    //定位的经度
    private double mLatitude;
    //定位的纬度
    private double mLongitude;

    public MyLocationHandler(Context mContext, AMap aMap) {
        this.mContext = mContext;
        this.mAMap = aMap;

        mOrientationListener = new MyOrientationListener(mContext);

        mOrientationListener.setOrientationChangeListener(new MyOrientationListener.OnOrientationChangeListener() {
            @Override
            public void onOritationChange(float x) {
                mCurrentX = x;
            }
        });

        //初始化AMap的信息：设置定位，图标
        initAMapData();
        initAmapIcon();
    }

    /**
     * 设置AMap
     */
    private void initAMapData() {

        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //设置定位监听
        mAMap.setLocationSource(this);
        // 是否可触发定位并显示定位层
        mAMap.setMyLocationEnabled(true);
        //设置缩放级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));

    }

    /**
     * 定义地图图标：如定位按钮，放大按钮，指南指
     */
    private void initAmapIcon() {

        //设置显示定位按钮 并且可以点击
        UiSettings settings = mAMap.getUiSettings();
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(false);
        // 启用指南针功能。
        settings.setCompassEnabled(true);
        //手指控制缩放
        settings.setZoomGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);
        //比例尺 调用 AMap.getScalePerPixel() 方法，根据用户当前地图的 zoom 值和地图中心点的坐标，可以获得当前比例尺的数据。
        settings.setScaleControlsEnabled(true);

        //定位的小图标 其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        myLocationStyle.anchor(0.5f, 0.5f);
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(android.R.color.transparent);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);

    }

    //初始化AMapLocationClient
    private void initLoc() {
        //初始化定位
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

    /**
     * 定位结果异步回调接口
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        LogUtils.d("onlocation changed....isFirstloc "+isFirstLoc);

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0 && mLocationChangeListener != null) {

                //定位成功后，改变城市和经纬度
                mCity = aMapLocation.getCity();
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();

                LogUtils.d("Latitude  "+mLatitude+"  Longitude  "+mLongitude);

                // 调用此方法显示系统小蓝点，也可以不调用此方法自己绘制marker
                mLocationChangeListener.onLocationChanged(aMapLocation);
                //设置当前位置的图标的旋转角度

                mAMap.setMyLocationRotateAngle(mCurrentX);

                //第一次定位时执行此方法，将用户位置移动到地图中心点，接下来的定位则不执行，
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //将地图移动到定位点
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));

                    //自己添加marker
//                    mMyLocationMarker = mAMap.addMarker(getMarkerOptions(aMapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                    ToastUtil.showCenterToast(mContext, buffer.toString());
                    isFirstLoc = false;
                }

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                String errInfo = "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo();
                LogUtils.d(errInfo);
                ToastUtil.showCenterToast(mContext, errInfo);
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        //位置
//        options.position(new LatLng(mLatitude, mLongitude));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
//        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
//        options.period(60);

        return options;

    }

    //激活定位，点击系统定位图标时执行的方法
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangeListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mLocationClient == null) {
            initLoc();
        }
        isFirstLoc = true;
        mOrientationListener.start();
        mLocationClient.startLocation();
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
        mLocationChangeListener = null;
    }

    public void onDestory() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mLocationChangeListener = null;

        if(mOrientationListener != null) {
            mOrientationListener.stop();
        }
        mOrientationListener = null;
        mAMap = null;
    }

    public void setFirstLoc(boolean firstLoc) {
        isFirstLoc = firstLoc;
    }

    public String getmCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Marker getmMyLocationMarker() {
        return mMyLocationMarker;
    }

    public void setmMyLocationMarker(Marker mMyLocationMarker) {
        this.mMyLocationMarker = mMyLocationMarker;
    }
}
