package com.wanghaisheng.template_lib.presenter.base;

import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sheng on 2016/5/31.
 */
public abstract class BaseLoadablePresenter<V extends BaseLoadableView> implements IPresenter<V> {

    //弱引用，防止内存泄露
    private WeakReference<V> viewRef;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();//这个是持有订阅  用于生命周期

    @Override
    public void attachView(V paramView) {
        viewRef = new WeakReference<V>(paramView);
    }

    @Override
    public void detachView() {
        if(null != this.viewRef) {
            viewRef.clear();
            viewRef = null;
        }

        if(null != compositeSubscription && compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public V getView() {
        if(null == this.viewRef) {
            return null;
        }

        return this.viewRef.get();
    }

    /**
     * 隐藏Loading页面
     */
    public void hideViewLoading() {
        if(isViewAttached()) {
            getView().hideLoading();
        }
    }

    /**
     * 显示Loading页面
     */
    public void showViewLoading() {
        if(isViewAttached()) {
            getView().showLoading();
        }
    }

    @Override
    public boolean isViewAttached() {
        return (null!=this.viewRef)&&(this.viewRef.get()!=null);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
