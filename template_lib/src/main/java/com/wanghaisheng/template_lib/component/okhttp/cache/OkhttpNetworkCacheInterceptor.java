package com.wanghaisheng.template_lib.component.okhttp.cache;

import com.wanghaisheng.template_lib.datasource.cache.CacheHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by sheng on 2016/7/26.
 */
public class OkhttpNetworkCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response.Builder resBuilder = chain.proceed(chain.request()).newBuilder();
        resBuilder.removeHeader("Pragma")
//                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                .header("Accept-Language","zh-CN,zh;q=0.8")
                .header("Cache-Control", String.format("max-age=%d", CacheHelper.OKHTTP_CACHE_TIME));
//                .header("Connection","keep-alive")
//                .header("DNT","1")
//                .header("Upgrade-Insecure-Requests","1");
        return resBuilder.build();
    }
}
