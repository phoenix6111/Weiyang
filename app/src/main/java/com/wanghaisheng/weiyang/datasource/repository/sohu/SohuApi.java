package com.wanghaisheng.weiyang.datasource.repository.sohu;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Author: sheng on 2016/8/8 15:46
 * Email: 1392100700@qq.com
 */
public interface SohuApi {

    @Headers({"User-Agent:Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"
    ,"Connection:keep-alive"})
    @GET("{tag}/index.js")
    Observable<SohuFirstResponseData> getIndexArticleBeanList(@Path("tag") String tag);

    @Headers({"User-Agent:Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"
            ,"Connection:keep-alive"})
    @GET("{tag}/index{page}.js")
    Observable<SohuFirstResponseData> getOtherArticleBeanList(@Path("tag") String tag, @Path("page") String page);

    @GET("n/{id}")
    Observable<String> getArticleDetail(@Path("id") String id);

}
