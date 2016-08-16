package com.wanghaisheng.weiyang.injector.module;

import android.content.Context;

import com.wanghaisheng.template_lib.component.okhttp.cookie.store.PersistentCookieStore;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.datasource.cache.ICacheManager;
import com.wanghaisheng.template_lib.datasource.repository.common.SearchTagRepository;
import com.wanghaisheng.template_lib.datasource.repository.common.SearchTagRepositoryImpl;
import com.wanghaisheng.template_lib.utils.PropsUtil;
import com.wanghaisheng.weiyang.database.ChannelDao;
import com.wanghaisheng.weiyang.datasource.repository.DataHandlerFactory;
import com.wanghaisheng.weiyang.datasource.repository.UserStorage;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapDetailApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapListApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapRepository;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapRepositoryImpl;
import com.wanghaisheng.weiyang.datasource.repository.common.CommonRepository;
import com.wanghaisheng.weiyang.datasource.repository.common.CommonRepositoryImpl;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoApi;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuApi;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepository;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepositoryImpl;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserRepository;
import com.wanghaisheng.weiyang.datasource.repository.user.UserRepositoryImpl;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinApi;
import com.wanghaisheng.weiyang.presenter.common.RequestWrapperFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by sheng on 2016/5/29.
 */

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    public ICache provideICache(Context context) {
        return new ICacheManager(context);
    }

    @Provides
    @Singleton
    public CommonRepository provideCommonRepository(ICache iCache, Context context, ChannelDao channelDao) {
        return new CommonRepositoryImpl(iCache,context,channelDao);
    }

    @Provides
    @Singleton
    public SearchTagRepository provideSearchTagRepository(ICache iCache) {
        return new SearchTagRepositoryImpl(iCache);
    }

    @Provides
    @Singleton
    public RequestWrapperFactory provideRequestWrapperFactory(DataHandlerFactory dataHandlerFactory,SohuRepository sohuRepository) {
        return new RequestWrapperFactory(dataHandlerFactory,sohuRepository);
    }

    @Provides
    @Singleton
    public SohuRepository provideSohuRepository(ICache iCache, SohuApi sohuApi) {
        return new SohuRepositoryImpl(iCache,sohuApi);
    }

    @Provides
    @Singleton
    public DataHandlerFactory provideDataHandlerFactory(ICache iCache, WechatArticleApi wechatArticleApi, YinApi yinApi,
        DouguoApi douguoApi, JianshuApi jianshuApi, ScienceArticleApi scienceArticleApi, SohuApi sohuApi, TTYYApi ttyyApi) {
        return new DataHandlerFactory(iCache, wechatArticleApi ,yinApi,douguoApi,jianshuApi,scienceArticleApi,sohuApi,ttyyApi);
    }

    @Provides
    @Singleton
    public UserStorage provideUserStorage(Context mContext, PropsUtil propsUtil, PersistentCookieStore cookieStore) {
        return new UserStorage(mContext,propsUtil,cookieStore);
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserApi userApi) {
        return new UserRepositoryImpl(userApi);
    }

    @Provides
    @Singleton
    public AMapRepository provideAMapRepository(ICache iCache, AMapListApi aMapListApi, AMapDetailApi aMapDetailApi) {
        return new AMapRepositoryImpl(iCache,aMapListApi,aMapDetailApi);
    }

}