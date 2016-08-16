package com.wanghaisheng.weiyang.presenter.auth;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;
import com.wanghaisheng.weiyang.datasource.repository.SchedulersCompat;
import com.wanghaisheng.weiyang.datasource.repository.UserStorage;
import com.wanghaisheng.weiyang.datasource.repository.user.UserRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/7/13.
 */
public class LoginPresenter extends BaseLoadablePresenter<AuthView> {

    @Inject
    UserRepository repository;
    @Inject
    UserStorage userStorage;

    public LoginPresenter() {}

    /**
     * 用户注册
     * @param tel
     * @param passwd
     */
    public void login(String tel,String passwd) {

        showViewLoading();

        Subscription subscription = repository.login(tel,passwd)
                .compose(SchedulersCompat.<UserResponseResult>applyUserResponseSchedulers())
                .doOnNext(new Action1<UserResponseResult>() {
                    @Override
                    public void call(final UserResponseResult result) {
                        if(null != result.getResult()) {
                            Observable.create(new Observable.OnSubscribe<Boolean>() {
                                @Override
                                public void call(Subscriber<? super Boolean> subscriber) {
                                    userStorage.saveUserInfo(result.getResult());
                                    subscriber.onNext(true);
                                    subscriber.onCompleted();
                                }
                            }).subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {

                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            LogUtils.d(throwable);
                                        }
                                    });
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<UserResponseResult>() {
                    @Override
                    protected void onError(AppException ex) {
                        AuthView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(UserResponseResult userResponseResult) {
                        AuthView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.handlerResult(true);
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }

}
