package com.wanghaisheng.template_lib.datasource.cache;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;

import java.io.Serializable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by sheng on 2016/5/30.
 */
public class ICacheManager implements ICache {


    private Context mContext;
    private ACache dataCache;

    public ICacheManager(Context context) {
        this.mContext = context;
        this.dataCache = ACache.get(mContext);
    }

    @Override
    public void saveObject(String key, Serializable obj, int lifeTime) {
        dataCache.put(key,obj,lifeTime);
    }

    @Override
    public void saveObject(String key, Serializable obj) {
        dataCache.put(key,obj);
    }

    @Override
    public Serializable getSerializable(String key) {
        return (Serializable) dataCache.getAsObject(key);
    }

    @Override
    public void saveString(String key, String value, int lifeTime) {
        dataCache.put(key,value,lifeTime);
    }

    @Override
    public void saveString(String key, String value) {
        dataCache.put(key,value);
    }

    @Override
    public String getString(String key) {
        return dataCache.getAsString(key);
    }

    public <T> Observable<T> getObservableCacheData(final String cacheKey) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T items = (T) getSerializable(cacheKey);
//                LogUtils.d(items);
                subscriber.onNext(items);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void clearCacheData() {
        dataCache.clear();
    }

}
