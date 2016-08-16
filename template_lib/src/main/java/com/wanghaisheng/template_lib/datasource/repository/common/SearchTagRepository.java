package com.wanghaisheng.template_lib.datasource.repository.common;

import java.util.List;

import rx.Observable;

/**
 * Created by sheng on 2016/6/7.
 */
public interface SearchTagRepository {

    Observable<List<String>> loadSearchHistoryTag(String cacheKey);

    Observable<Boolean> saveSearchHistoryTag(String cacheKey, List<String> tags);

}
