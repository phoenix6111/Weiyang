package com.wanghaisheng.weiyang.datasource.repository.amap;

import com.wanghaisheng.weiyang.datasource.beans.MapPoiBeanResult;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: sheng on 2016/8/15 18:12
 * Email: 1392100700@qq.com
 */
public interface AMapListApi {

    @Headers({"User-Agent:Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36," +
            "X-Requested-With:XMLHttpRequest","Host:m.amap.com","Connection:keep-alive"})
    @GET("service/poi/keywords.json")
    Observable<MapPoiBeanResult> getAMapPoiList(@Query("keywords") String keywords, @Query("latitude") double latitude, @Query("longitude") double longitude,
                                                @Query("pagenum") int pagenum, @Query("user_loc") String user_loc, @Query("type") String type, @Query("client_network_class") int client_network_class,
                                                @Query("cluster_state") int cluster_state);

    //  keywords=美食&latitude=22.691026&longitude=114.379392&pagenum=1&type=nearby
    // &user_loc=114.379392%2C22.691026&client_network_class=4&cluster_state=5
}
