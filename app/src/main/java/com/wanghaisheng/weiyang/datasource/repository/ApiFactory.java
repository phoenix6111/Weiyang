package com.wanghaisheng.weiyang.datasource.repository;

import com.wanghaisheng.weiyang.datasource.repository.amap.AMapDetailApi;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapListApi;
import com.wanghaisheng.weiyang.datasource.repository.converter.AppConverterFactory;
import com.wanghaisheng.weiyang.datasource.repository.converter.DouguoContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.JianshuContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.ScienceContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.SohuContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.SohuConverterFactory;
import com.wanghaisheng.weiyang.datasource.repository.converter.WechatArticleContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.YinContentParser;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoApi;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuApi;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuApi;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYApi;
import com.wanghaisheng.weiyang.datasource.repository.user.UserApi;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sheng on 2016/5/29.
 */
public class ApiFactory {

    public static final String WECHATARTICLE_BASE_URL = "http://weixin.sogou.com/";
    public static final String YIN_BASE_URL = "http://m.yinews.cn/";
    public static final String DOUGUO_BASE_URL = "http://www.douguo.com/";
    public static final String JIANSHU_BASE_URL = "http://www.jianshu.com/";
    public static final String SCIENCEARTICLE_BASE_URL = "http://www.kexuelife.com/";
    public static final String SOHU_BASE_URL = "http://chihe.sohu.com/";
    public static final String TTYY_BASE_URL = "http://m.51ttyy.com/";
    public static final String USER_BASE_URL = "http://justgogo.biz/app/xiaoya/";
    public static final String AMAP_POI_LIST_URL = "http://m.amap.com/";
    public static final String AMAP_POI_DETAIL_URL = "http://ditu.amap.com/";

    private OkHttpClient mHttpClient;

    public ApiFactory(OkHttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    public WechatArticleApi produceWechatArticleApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WECHATARTICLE_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(AppConverterFactory.create(new WechatArticleContentParser()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(WechatArticleApi.class);
    }


    public YinApi produceYinApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YIN_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(AppConverterFactory.create(new YinContentParser()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(YinApi.class);
    }

    public DouguoApi produceDouguoApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DOUGUO_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(AppConverterFactory.create(new DouguoContentParser()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(DouguoApi.class);
    }

    public JianshuApi produceJianshuApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(JIANSHU_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(AppConverterFactory.create(new JianshuContentParser()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(JianshuApi.class);
    }

    public ScienceArticleApi produceScienceArticleApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SCIENCEARTICLE_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(AppConverterFactory.create(new ScienceContentParser()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ScienceArticleApi.class);
    }

    public SohuApi produceSohuApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SOHU_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(SohuConverterFactory.create(new SohuContentParser()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(SohuApi.class);
    }

    public TTYYApi produceTTYYApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TTYY_BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(TTYYApi.class);
    }

    public UserApi produceUserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(USER_BASE_URL)
                .client(mHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(UserApi.class);
    }

    public AMapListApi produceAMapListApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AMAP_POI_LIST_URL)
                .client(mHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AMapListApi.class);
    }

    public AMapDetailApi produceAMapDetailApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AMAP_POI_DETAIL_URL)
                .client(mHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AMapDetailApi.class);
    }

}
