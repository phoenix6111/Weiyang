package com.wanghaisheng.template_lib.datasource.repository.common;

import com.wanghaisheng.template_lib.datasource.cache.ICache;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by sheng on 2016/6/7.
 */
public class SearchTagRepositoryImpl implements SearchTagRepository{
    protected ICache iCache;

    public SearchTagRepositoryImpl(ICache iCache) {
        this.iCache = iCache;
    }

    /**
     * 加载所有搜索tag
     * @return
     */
    @Override
    public Observable<List<String>> loadSearchHistoryTag(final String cacheKey) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> searchTags = (List<String>) iCache.getSerializable(cacheKey);
                subscriber.onNext(searchTags);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());


    }

    /**
     * 保存搜索tag
     * @param tags
     * @return
     */
    @Override
    public Observable<Boolean> saveSearchHistoryTag(final String cacheKey, final List<String> tags) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                iCache.saveObject(cacheKey, (Serializable) tags);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());

    }

}
