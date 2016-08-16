package com.wanghaisheng.template_lib.presenter.base;

import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sheng on 2016/6/12.
 */
public abstract class BasePresenter <V extends IView> implements IPresenter<V> {

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
