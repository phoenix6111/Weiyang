package com.wanghaisheng.weiyang.datasource.repository.science;

import com.wanghaisheng.weiyang.datasource.beans.ScienceArticleResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/6/10.
 */
public interface ScienceArticleApi {

    @GET("apis/article.json")
    Observable<ScienceArticleResult> getIndexScienceArticleResult(@Query("offset") int offset, @Query("limit") int limit);

    @GET("apis/article.json")
    Observable<ScienceArticleResult> getArticleBeanList(@Query("retrieve_type") String retrieveType
            , @Query("tag_id") String tagId, @Query("offset") int offset, @Query("limit") int limit);

    /**
     * 获取页面详情
     * @param articleLink
     * @return
     */
    @GET("{articleLink}")
    Observable<String> getArticleBeanDetail(@Path("articleLink") String articleLink);
}
