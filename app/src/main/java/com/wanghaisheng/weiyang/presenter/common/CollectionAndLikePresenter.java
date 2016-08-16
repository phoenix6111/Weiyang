package com.wanghaisheng.weiyang.presenter.common;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BasePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.database.MeishiBeanDao;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sheng on 2016/6/23.
 */
public class CollectionAndLikePresenter extends BasePresenter<CollectionView> {

    @Inject
    Lazy<MeishiBeanDao> meishiDaoLazy;

    @Inject
    public CollectionAndLikePresenter(){}

    public void checkIfCollected(final BaseBean baseBean) {
        Observable<Boolean> observable = null;

        if(baseBean instanceof MeishiBean) {
            final MeishiBean articleBean = (MeishiBean) baseBean;
            observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    List<MeishiBean> articles = meishiDaoLazy.get().queryBuilder()
                            .where(MeishiBeanDao.Properties.ArticleUrl.eq(articleBean.getArticleUrl())
                                    , MeishiBeanDao.Properties.IsCollected.eq(true)).list();
                    if(!ListUtils.isEmpty(articles)) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onNext(false);
                    }

                    subscriber.onCompleted();
                }
            });
        }

        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<Boolean>() {
                    @Override
                    protected void onError(AppException ex) {
                        CollectionView iView = getView();
                        if(null != iView) {
                            iView.loadError(IView.LOAD_TYPE_CHECK_COLLECT,ex);
                        }
                    }

                    @Override
                    public void onNext(Boolean result) {
                        CollectionView iView = getView();
                        if(null != iView) {
                            iView.updateCheckResult(result);
                        }
                    }
                });

        compositeSubscription.add(subscription);

    }

    //收藏或取消收藏article
    public void collectOrUnCollect(final BaseBean baseBean) {

        LogUtils.d(baseBean);

        Observable<BaseBean> observable = null;
        if(baseBean instanceof MeishiBean) {
            final MeishiBean articleBean = (MeishiBean) baseBean;
            observable = Observable.create(new Observable.OnSubscribe<BaseBean>() {
                @Override
                public void call(Subscriber<? super BaseBean> subscriber) {

                    meishiDaoLazy.get().insertOrReplace(articleBean);
                    subscriber.onNext(articleBean);
                    subscriber.onCompleted();
                }
            });
        }

        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<BaseBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        CollectionView iView = getView();
                        if(null != iView) {
                            iView.loadError(IView.LOAD_TYPE_COLLECT,ex);
                        }
                    }

                    @Override
                    public void onNext(BaseBean result) {
                        CollectionView iView = getView();
                        if(null != iView) {
                            if(result.getIsCollected()) {
                                iView.updateCollectionResult(true);
                            } else {
                                iView.updateCollectionResult(false);
                            }
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }



}