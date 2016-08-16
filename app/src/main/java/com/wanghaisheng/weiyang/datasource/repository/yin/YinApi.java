package com.wanghaisheng.weiyang.datasource.repository.yin;

import com.wanghaisheng.weiyang.datasource.beans.YinBeanResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/6/20.
 */
public interface YinApi {

    @GET("ArticleApi/GetArticleListByCid")
    Observable<YinBeanResult> getArticleBeanList(@Query("cid") String cid, @Query("pi") int page, @Query("ps") int ps);

    @GET("{purl}")
    Observable<String> getArticleBeanDetail(@Path("purl") String purl);
}
