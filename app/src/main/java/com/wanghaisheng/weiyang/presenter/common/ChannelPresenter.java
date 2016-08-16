package com.wanghaisheng.weiyang.presenter.common;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BasePresenter;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.datasource.repository.common.CommonRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/6/5.
 */
public class ChannelPresenter extends BasePresenter<ChannelView> {
    private static final String TAG = "ChannelPresenter";

    @Inject
    CommonRepository repository;

    @Inject
    public ChannelPresenter(){}


    public void getSyncChannelEntities(final String module) {
        Subscription subscrible = Observable.create(new Observable.OnSubscribe<List<ZhuantiChannelEntity>>() {
            @Override
            public void call(Subscriber<? super List<ZhuantiChannelEntity>> subscriber) {
                subscriber.onNext(repository.getChannelEntitiesByModule(module));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<List<ZhuantiChannelEntity>>() {
                    @Override
                    protected void onError(AppException ex) {
                        LogUtils.d(ex);
                    }

                    @Override
                    public void onNext(List<ZhuantiChannelEntity> channelEntities) {
                        ChannelView iView = getView();
                        if(iView != null) {
//                            LogUtils.d("render channel bean..");
                            iView.renderSavedChannelBean(channelEntities);
                        }
                    }
                });
        compositeSubscription.add(subscrible);
    }

    public List<ZhuantiChannelEntity> getChannelEntities(final String module) {
        return repository.getChannelEntitiesByModule(module);
    }

}
