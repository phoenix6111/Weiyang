package com.wanghaisheng.weiyang.datasource.repository.amap;

import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBeanResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: sheng on 2016/8/15 18:46
 * Email: 1392100700@qq.com
 */
public interface AMapDetailApi {

    @GET("detail/get/detail")
    Observable<MapPoiDetailBeanResult> getMapDetailBean(@Query("id") String poiId);

    //detail/get/detail?id=B0FFG69KQ8
}
