package com.wanghaisheng.template_lib.datasource.cache;

import java.io.Serializable;

import rx.Observable;

/**
 * Created by sheng on 2016/5/30.
 */
public interface ICache {
    int TIME_MINUTE = 60;//缓存有效期单位
    int TIME_HOUR = 60 * 60;
    int TIME_DAY = TIME_HOUR * 24;

    void saveObject(String key, Serializable obj, int lifeTime);
    void saveObject(String key, Serializable obj);
    Serializable getSerializable(String key);

    void saveString(String key, String value, int lifeTime);
    void saveString(String key, String value);
    String getString(String key);

    <T> Observable<T> getObservableCacheData(final String cacheKey);

    void clearCacheData();

}
