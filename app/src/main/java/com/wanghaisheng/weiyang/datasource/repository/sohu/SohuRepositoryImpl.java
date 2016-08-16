package com.wanghaisheng.weiyang.datasource.repository.sohu;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.EntitiesToArticles;
import com.wanghaisheng.weiyang.datasource.repository.converter.SohuContentParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: sheng on 2016/8/8 15:47
 * Email: 1392100700@qq.com
 */
public class SohuRepositoryImpl implements ArticleBeanDataHandler,SohuRepository{

    private SohuApi sohuApi;
    private ICache iCache;

    public SohuRepositoryImpl(ICache iCache ,SohuApi sohuApi) {
        this.sohuApi = sohuApi;
        this.iCache = iCache;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        final MeishiBean meishiBean = (MeishiBean) baseBean;
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                OkHttpUtils.get().url("http://m.sohu.com/n/"+meishiBean.getArticleId()+"/")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.d(e);
                    }

                    @Override
                    public void onResponse(String response) {
                        String result = SohuContentParser.parseArticleBeanDetail2(response);
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                });
            }
        });
//        return sohuApi.getArticleDetail(meishiBean.getArticleId());
    }

    /**
     * 从网络加载并保存到缓存
     * @param category
     * @param tag
     * @param page
     * @return
     */
    public Observable<SohuData> networkArticleBeanResult(final String category, final String tag, final int page) {
        LogUtils.d(page);
        Observable<SohuFirstResponseData> observable = null;
        if(page <= 1) {
            observable = sohuApi.getIndexArticleBeanList(tag);
        } else {
            String rPage = "_"+page;
            observable = sohuApi.getOtherArticleBeanList(tag,rPage);
        }

        return observable.map(new Func1<SohuFirstResponseData, SohuData>() {
                    @Override
                    public SohuData call(SohuFirstResponseData sohuFirstResponseData) {
                        SohuData sohuData = new SohuData();
                        sohuData.setPage(sohuFirstResponseData.getCount());
                        List<BaseBean> list = EntitiesToArticles.sohuArticlesToArticleBeans(sohuFirstResponseData.getPage());
                        sohuData.setList(list);

                        return sohuData;
                    }
                }).doOnNext(new Action1<SohuData>() {
            @Override
            public void call(final SohuData sohuData) {
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        LogUtils.d(sohuData.getList());
                        if(ListUtils.isEmpty(sohuData.getList())) {
                            subscriber.onNext(false);
                            subscriber.onCompleted();
                            return;
                        }

                        final String cacheKey = CacheHelper.getCacheKey(getCachePrefix(""),category,tag,page);
                        Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                iCache.saveObject(cacheKey, (Serializable) sohuData,getCacheTime());
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {

                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtils.d(throwable);
                                    }
                                });
                    }
                });
            }
        });

    }

    /**
     * 先从缓存加载，缓存没有则从网络加载
     * @param category
     * @param tag
     * @param page
     * @return
     */
    @Override
    public Observable<SohuData> subscribeArticleBean(String category, String tag, int page) {
        final String cacheKey = CacheHelper.getCacheKey(getCachePrefix(""),"xxx",tag,page);
        Observable<SohuData> diskCache = iCache.getObservableCacheData(cacheKey);

        return Observable.concat(
                diskCache
                ,networkArticleBeanResult(category,tag,page))
                .first(new Func1<SohuData, Boolean>() {
                    @Override
                    public Boolean call(SohuData datas) {
//                        LogUtils.d(datas);
                        return datas!=null&&!ListUtils.isEmpty(datas.getList());
                    }
                });
    }

    @Override
    public Observable<String> loadMoreContent(final String id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                OkHttpUtils.get().url("http://m.sohu.com/api/n/v3/rest/"+id+"/")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.d(e);
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(String response) {
//                        LogUtils.d(response);
                        if(TextUtils.isEmpty(response)) {
                            subscriber.onNext("没有更多内容");
                            subscriber.onCompleted();
                            return;
                        }
                        Gson gson = new Gson();
                        SohuMoreContent content = gson.fromJson(response,SohuMoreContent.class);
//                        LogUtils.d(content.getRest_content());
                        subscriber.onNext(content.getRest_content());
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }


    public String getCachePrefix(String module) {
        return ApiFactory.SOHU_BASE_URL;
    }

    public int getCacheTime() {
        return CacheHelper.MIDULE_CACHE_TIME;
    }
}
