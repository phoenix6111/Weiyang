package com.wanghaisheng.weiyang.datasource.repository.jianshu;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/5/10.
 */
public interface JianshuApi {

    /**
     * 根据专题号和page查询
     * @param CId
     * @param orderBy
     * @param page
     * @return
     */
    @GET("collections/{cid}/notes")
    Observable<List<BaseBean>> getArticleBeanList(@Path("cid") String CId, @Query("order_by") String orderBy, @Query("page") int page);

    /**
     * 根据页面的id获取页面详情
     * @param purl
     * @return
     */
    @Headers({"User-Agent:Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"})
    @GET("{purl}")
    Observable<String> getArticleBeanDetail(@Path("purl") String purl);
}
