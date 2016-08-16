package com.wanghaisheng.weiyang.datasource.repository;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import rx.Observable;

/**
 * Created by sheng on 2016/7/3.
 */
public interface ArticleBeanDataHandler {

    Observable<String> getArticleBeanDetail(BaseBean baseBean);


}
