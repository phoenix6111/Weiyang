package com.wanghaisheng.weiyang.datasource.repository.amap;

import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBeanResult;

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

    Observable<List<MapPoiBean>> subscribeMapPoiBeanList(double latitude,double longitude,int pageNum);

    /**
     * 根据PoiId 获取MapPoiDetailBean
     * @param poiId
     * @return
     */
    Observable<MapPoiDetailBean> getMapPoiDetailBean(MapPoiBean mapPoiBean);

}
