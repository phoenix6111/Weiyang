package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Author: sheng on 2016/8/13 22:56
 * Email: 1392100700@qq.com
 * 方向传感器监听器，监听用户手机方向变化
 */
public class MyOrientationListener implements SensorEventListener {

    private Context mContext;
    private Sensor mSensor;
    //传感器管理类
    private SensorManager mSensorManager;

    //保存上一次x轴
    private float lastX;

    public MyOrientationListener(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 暴露给外部调用方法，启动传感器监听
     */
    public void start() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager != null) {
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        /**
         * 如果手机支持方向传感器
         */
        if(mSensor != null && mSensorManager != null) {
            mSensorManager.registerListener(this,mSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * stop传感器监听
     */
    public void stop() {
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //如果是方向改变
        if(Sensor.TYPE_ORIENTATION == event.sensor.getType()) {
            //获取x轴上的变化
            float x = event.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX) > 1.0) {
                if(mOrientationChangeListener != null) {
                    mOrientationChangeListener.onOritationChange(x);
                }
                lastX = x;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private OnOrientationChangeListener mOrientationChangeListener;


    public void setOrientationChangeListener(OnOrientationChangeListener mOrientationChangeListener) {
        this.mOrientationChangeListener = mOrientationChangeListener;
    }

    /**
     * 当方向改变时的回调接口
     */
    public interface OnOrientationChangeListener{
        void onOritationChange(float x);
    }
}
