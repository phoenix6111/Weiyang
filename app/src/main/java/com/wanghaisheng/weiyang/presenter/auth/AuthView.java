package com.wanghaisheng.weiyang.presenter.auth;


import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;

/**
 * Created by sheng on 2016/5/22.
 */
public interface AuthView extends BaseLoadableView {
    void handlerResult(boolean success);
}
