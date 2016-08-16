package com.wanghaisheng.template_lib.presenter.base;

import com.wanghaisheng.template_lib.appexception.AppException;

/**
 * Created by sheng on 2016/6/12.
 */
public interface IView {

    int LOAD_TYPE_FIRSTLOAD = 1;//加载类型：第一次加载
    int LOAD_TYPE_REFRESH = 2;//加载类型：刷新
    int LOAD_TYPE_LOADMORE = 3;//加载类型：加载更多
    int LOAD_TYPE_COLLECT = 4;//操作类型：收藏
    int LOAD_TYPE_UNCOLLECT = 5;//操作类型：取消收藏
    int LOAD_TYPE_CHECK_COLLECT = 6;//操作类型，检测是否收藏
    int LOAD_TYPE_SEARCH_HISTORY = 7;//操作类型，加载搜索记录
    int LOAD_TYPE_BACK = 8;//操作类型，后台加载

    /**
     * 加载错误
     * @param loadType 加载的类型：第一次加载，刷新，加载更多，因为这三种状态UI层处理结果不同
     * @param e ，自定义AppException，供UI层处理
     */
    void loadError(int loadType, AppException e);

}
