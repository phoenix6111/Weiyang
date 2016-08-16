package com.wanghaisheng.weiyang.injector.component;

import android.content.Context;

import com.wanghaisheng.template_lib.component.okhttp.cookie.store.PersistentCookieStore;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.datasource.repository.common.SearchTagRepository;
import com.wanghaisheng.template_lib.utils.ConfigHelper;
import com.wanghaisheng.template_lib.utils.FileHelper;
import com.wanghaisheng.template_lib.utils.FormatHelper;
import com.wanghaisheng.template_lib.utils.NetWorkHelper;
import com.wanghaisheng.template_lib.utils.PrefsUtil;
import com.wanghaisheng.template_lib.utils.PropsUtil;
import com.wanghaisheng.template_lib.utils.RequestHelper;
import com.wanghaisheng.template_lib.utils.ResourceHelper;
import com.wanghaisheng.template_lib.utils.SecurityHelper;
import com.wanghaisheng.template_lib.utils.SettingPrefHelper;
import com.wanghaisheng.template_lib.utils.StringHelper;
import com.wanghaisheng.template_lib.utils.ToastHelper;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.database.ChannelDao;
import com.wanghaisheng.weiyang.database.MeishiBeanDao;
import com.wanghaisheng.weiyang.datasource.repository.DataHandlerFactory;
import com.wanghaisheng.weiyang.datasource.repository.UserStorage;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapDetailApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapListApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapRepository;
import com.wanghaisheng.weiyang.datasource.repository.common.CommonRepository;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoApi;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuApi;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepository;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserRepository;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinApi;
import com.wanghaisheng.weiyang.injector.module.ApiModule;
import com.wanghaisheng.weiyang.injector.module.AppModule;
import com.wanghaisheng.weiyang.injector.module.DataSourceModule;
import com.wanghaisheng.weiyang.injector.module.DbModule;
import com.wanghaisheng.weiyang.injector.module.HelperModule;
import com.wanghaisheng.weiyang.presenter.common.RequestWrapperFactory;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;


/**
 * Created by sheng on 2016/4/13.
 */

@Component(modules = {AppModule.class, ApiModule.class, DbModule.class,
        HelperModule.class, DataSourceModule.class})
@Singleton //用singleton表明该component所对应的module有singleton方法
public interface AppComponent {

    //给其它Component提供依赖时，必须有该对象的返回值
    Context getContext();

    OkHttpClient getOkHttpClient();

    NetWorkHelper getNetWorkHelper();
    SecurityHelper getSecurityHelper();
    FileHelper getFileHelper();
    ConfigHelper getConfigHelper();
    ResourceHelper getResourceHelper();
    ToastHelper getToastHelper();
    SettingPrefHelper getSettingPrefHelper();
    FormatHelper getFormatHelper();
    StringHelper getStringHelper();
    RequestHelper getRequestHelper();
    PrefsUtil getPrefsUtil();
    PropsUtil getPropsUtil();
    PersistentCookieStore getCookieStore();

    //DB Module
    ChannelDao getChannelDao();
    MeishiBeanDao getMeishiBeanDao();

    //API
    WechatArticleApi getWechatArticleApi();
    DouguoApi getDouguoApi();
    YinApi getYinApi();
    JianshuApi getJianshuApi();
    ScienceArticleApi getScienceArticleApi();
    SohuApi getSohuApi();
    TTYYApi getTTYYApi();
    UserApi getUserApi();
    AMapListApi getAMapListApi();
    AMapDetailApi getAMapDetailApi();

    //repository
    ICache getICache();
    CommonRepository getCommonRepository();
    SearchTagRepository getSearchTagRepository();
    RequestWrapperFactory getRequestWrapperFactory();
    DataHandlerFactory getDataHandlerFactory();
    SohuRepository getSohuRepository();
    UserStorage getUserStorage();
    UserRepository getUserRepository();
    AMapRepository getAMapRepository();

    void inject(AppContext appContext);

}
