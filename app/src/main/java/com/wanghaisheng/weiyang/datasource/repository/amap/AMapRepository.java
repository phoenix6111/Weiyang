package com.wanghaisheng.weiyang.datasource.repository.amap;

import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;

import java.util.List;

import rx.Observable;

/**
 * Author: sheng on 2016/8/15 18:11
 * Email: 1392100700@qq.com
 */
public interface AMapRepository {

    /**
     * 根据经纬度和pagenum获取MapPoiBean
     * @param latitude
     * @param longitude
     * @param pageNum
     * @return
     */
    Observable<List<MapPoiBean>> networkMapPoiBeanList(double latitude,double longitude,int pageNum);

    Observable<List<MapPoiBean>> subscribeMapPoiBeanList(double latitude, double longitude, int pageNum);

    /**
     * mapPoiBean 获取MapPoiDetailBean
     * @param mapPoiBean
     * @return
     */
    Observable<MapPoiDetailBean> netWorkMapPoiBeanDetail(MapPoiBean mapPoiBean);

    /**
     * 从缓存到网络顺序获取
     * @param mapPoiBean
     * @return
     */
    Observable<MapPoiDetailBean> subscribeMapPoiBeanDetail(final MapPoiBean mapPoiBean);


}
