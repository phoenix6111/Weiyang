package com.wanghaisheng.template_lib.presenter.base;

/**
 * Created by sheng on 2016/5/31.
 */
public interface IPresenter<V extends IView> {
    void attachView(V paramView);
    void detachView();
    void destroy();
    V getView();
    boolean isViewAttached();

    void pause();
    void resume();
}
