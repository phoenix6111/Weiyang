package com.wanghaisheng.weiyang.presenter.auth;


import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;

/**
 * Created by sheng on 2016/7/13.
 */
public interface UpdatePasswordView extends BaseLoadableView {

    void handlerResult(int type, boolean success);

    void showWaitDialog(String msg);
}
