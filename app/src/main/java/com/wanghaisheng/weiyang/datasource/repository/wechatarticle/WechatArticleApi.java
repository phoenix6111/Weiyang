package com.wanghaisheng.weiyang.datasource.repository.wechatarticle;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/5/16.
 */
public interface WechatArticleApi {

    @GET("weixin")
    Observable<List<BaseBean>> searchWechatArticleList(@Query("page") int page, @Query("query") String query, @Query("type") int type);

    @GET("wapindex/wap/0612/{category}/{page}.html")
    Observable<List<BaseBean>> getArticleBeanList(@Path("category") String category, @Path("page") int page);
}
