package com.wanghaisheng.weiyang.datasource.repository.ttyy;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: sheng on 2016/8/9 10:40
 * Email: 1392100700@qq.com
 */
public interface TTYYApi {

//    http://m.51ttyy.com/article/index_ajax_list.html?action=ajax&p=3
    @Headers({"User-Agent:Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36",
    "X-Requested-With:XMLHttpRequest","Accept:application/json, text/javascript, */*; q=0.01"})
    @GET("article/index_ajax_list.html")
    Observable<TTYYBeanResult> getArticleBeanList(@Query("p") int page,@Query("action") String action);

}
