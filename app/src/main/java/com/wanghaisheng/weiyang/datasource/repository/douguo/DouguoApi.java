package com.wanghaisheng.weiyang.datasource.repository.douguo;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sheng on 2016/7/4.
 */
public interface DouguoApi {

    /**
     * 获取美食列表
     * @param tag
     * @param offset
     * @return
     */
    @GET("caipu/{tag}/{offset}")
    Observable<List<BaseBean>> getCaipuBeanList(@Path("tag") String tag, @Path("offset") int offset);

    @GET("caipu/{offset}")
    Observable<List<BaseBean>> getIndexBaseBeanList(@Path("offset") int offset);

    @GET("search/recipe/{tag}/{offset}")
    Observable<List<BaseBean>> searchBaseBeanList(@Path("tag") String tag, @Path("offset") int offset);
}
