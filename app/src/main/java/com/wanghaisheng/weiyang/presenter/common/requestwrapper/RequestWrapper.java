package com.wanghaisheng.weiyang.presenter.common.requestwrapper;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

import rx.Observable;

/**
 * Created by sheng on 2016/6/15.
 * 请求包装类，自己处理 内部变量（page 或 offset 或 maxTime）与发送网络请求
 */
public interface RequestWrapper {

    /**
     * 获取ArticleBean list
     * @return
     */
    Observable<List<BaseBean>> getBeanList();

    Observable<List<BaseBean>> refreshList();

    /**
     * 当调用端获得订阅时，内部变量翻到下一页：如page,offset,maxTime
     */
    void nextIndex();

    void setPage(int page);

}
