package com.wanghaisheng.weiyang.injector.module;

import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapDetailApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapListApi;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoApi;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuApi;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuApi;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserApi;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by sheng on 2016/4/13.
 */

/**
 * Api 模块的 module
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    public ApiFactory provideApiFactory(OkHttpClient httpClient) {
        return new ApiFactory(httpClient);
    }

    @Provides
    @Singleton
    public WechatArticleApi provideWechatArticleApi(ApiFactory apiFactory) {
        return apiFactory.produceWechatArticleApi();
    }

    @Provides
    @Singleton
    public DouguoApi provideDouguoApi(ApiFactory apiFactory) {
        return apiFactory.produceDouguoApi();
    }

    @Provides
    @Singleton
    public YinApi provideYinApi(ApiFactory apiFactory) {
        return apiFactory.produceYinApi();
    }

    @Provides
    @Singleton
    public JianshuApi provideJianshuApi(ApiFactory apiFactory) {
        return apiFactory.produceJianshuApi();
    }

    @Provides
    @Singleton
    public ScienceArticleApi provideScienceArticleApi(ApiFactory apiFactory) {
        return apiFactory.produceScienceArticleApi();
    }

    @Provides
    @Singleton
    public SohuApi provideSohuApi(ApiFactory apiFactory) {
        return apiFactory.produceSohuApi();
    }

    @Provides
    @Singleton
    public TTYYApi provideTTYYApi(ApiFactory apiFactory) {
        return apiFactory.produceTTYYApi();
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(ApiFactory apiFactory) {
        return apiFactory.produceUserApi();
    }

    @Provides
    @Singleton
    public AMapListApi produceAMapListApi(ApiFactory apiFactory) {
        return apiFactory.produceAMapListApi();
    }

    @Provides
    @Singleton
    public AMapDetailApi produceAMapDetailApi(ApiFactory apiFactory) {
        return apiFactory.produceAMapDetailApi();
    }
}
