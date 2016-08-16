package com.wanghaisheng.weiyang.injector.module;

import android.content.Context;

import com.wanghaisheng.template_lib.component.okhttp.HttpLoggingInterceptor;
import com.wanghaisheng.template_lib.component.okhttp.cache.OkhttpNetworkCacheInterceptor;
import com.wanghaisheng.template_lib.component.okhttp.cookie.CookieJarImpl;
import com.wanghaisheng.template_lib.component.okhttp.cookie.store.PersistentCookieStore;
import com.wanghaisheng.weiyang.BuildConfig;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by sheng on 2016/4/13.
 */
@Module
public class AppModule {

    Context context;

    //全局context，使用application context
    public AppModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    PersistentCookieStore provideCookieStore(Context context) {
        return new PersistentCookieStore(context);
    }
    @Provides
    @Singleton//提供全局OkHttpClient
    public OkHttpClient provideOkHttpClient(PersistentCookieStore cookieStore) {
        CookieJarImpl cookieJar = new CookieJarImpl(cookieStore);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(CacheHelper.OKHTTP_CACHE_PATH), CacheHelper.OKHTTP_CACHE_SIZE))
                .cookieJar(cookieJar);

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(logging);
        }

        //okhttp network缓存
        builder.addNetworkInterceptor(new OkhttpNetworkCacheInterceptor());
        return builder.build();
    }


}
