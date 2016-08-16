package com.wanghaisheng.weiyang.datasource.repository;


import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

import rx.Observable;

/**
 * Created by sheng on 2016/7/1.
 */
public interface IDataHandler {

    Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page);

    /**
     * 获取缓存前缀
     * @param module
     * @return
     */
    String getCachePrefix(String module);

    /**
     * 获取缓存时间
     * @return
     */
    int getCacheTime();


}
