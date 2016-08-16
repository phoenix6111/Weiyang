package com.wanghaisheng.weiyang.datasource.repository.amap;

import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBeanResult;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBeanResult;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.EntitiesToArticles;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: sheng on 2016/8/15 19:43
 * Email: 1392100700@qq.com
 */
public class AMapRepositoryImpl implements AMapRepository {
    public static final String KEYWORD = "美食";

    private ICache iCache;
    private AMapListApi aMapListApi;
    private AMapDetailApi aMapDetailApi;

    public AMapRepositoryImpl(ICache iCache,AMapListApi aMapListApi,AMapDetailApi aMapDetailApi) {
        this.iCache = iCache;
        this.aMapListApi = aMapListApi;
        this.aMapDetailApi = aMapDetailApi;
    }

    @Override
    public Observable<List<MapPoiBean>> networkMapPoiBeanList(final double latitude, final double longitude, final int pageNum) {
        return aMapListApi.getAMapPoiList(KEYWORD,latitude,longitude,pageNum,longitude+","+latitude
                ,"nearby",4,5)
                .map(new Func1<MapPoiBeanResult, List<MapPoiBean>>() {
                    @Override
                    public List<MapPoiBean> call(MapPoiBeanResult mapPoiBeanResult) {
                        return EntitiesToArticles.parseAMapPoiListResultToAMapPoiList(mapPoiBeanResult);
                    }
                })
                .doOnNext(new Action1<List<MapPoiBean>>() {
                    @Override
                    public void call(final List<MapPoiBean> mapPoiBeen) {
                        Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                if(!ListUtils.isEmpty(mapPoiBeen)) {
                                    String cacheKey = CacheHelper.getCacheKey(ApiFactory.AMAP_POI_LIST_URL,latitude+"",longitude+"",pageNum);
                                    iCache.saveObject(cacheKey, (Serializable) mapPoiBeen);
                                }

                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<MapPoiBean>> subscribeMapPoiBeanList(double latitude, double longitude, int pageNum) {
        String cacheKey = CacheHelper.getCacheKey(ApiFactory.AMAP_POI_LIST_URL,latitude+"",longitude+"",pageNum);
        Observable<List<MapPoiBean>> diskCache = iCache.getObservableCacheData(cacheKey);

        return Observable.concat(
                diskCache
                ,networkMapPoiBeanList(latitude,longitude,pageNum))
                .first(new Func1<List<MapPoiBean>, Boolean>() {
                    @Override
                    public Boolean call(List<MapPoiBean> datas) {
                        return !ListUtils.isEmpty(datas);
                    }
                });

    }

    @Override
    public Observable<MapPoiDetailBean> getMapPoiDetailBean(final MapPoiBean mapPoiBean) {
        return aMapDetailApi.getMapDetailBean(mapPoiBean.getPoiId())
                .map(new Func1<MapPoiDetailBeanResult, MapPoiDetailBean>() {
                    @Override
                    public MapPoiDetailBean call(MapPoiDetailBeanResult mapPoiDetailBeanResult) {
                        return EntitiesToArticles.parseMapPoiDetailResultToMapPoiDetailBean(mapPoiDetailBeanResult,mapPoiBean.getDistance());
                    }
                })
                .doOnNext(new Action1<MapPoiDetailBean>() {
                    @Override
                    public void call(final MapPoiDetailBean mapPoiBeen) {
                        Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                String cacheKey = CacheHelper.getCacheKey(ApiFactory.AMAP_POI_DETAIL_URL,"xxx","detail",mapPoiBean.getPoiId());
                                iCache.saveObject(cacheKey, mapPoiBeen);

                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io());
    }


}
