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
import com.wanghaisheng.weiyang.ui.auth.UpdatePasswordActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/7/14.
 */
public class UpdatePasswordPresenter extends BaseLoadablePresenter<UpdatePasswordView> {

    @Inject
    UserRepository repository;
    @Inject
    UserStorage userStorage;

    @Inject
    public UpdatePasswordPresenter() {}

    /**
     * 检测电话号码是否存在
     * @param tel
     */
    public void validateServerTelExists(String tel) {
        UpdatePasswordView updateView = getView();
        if(updateView != null) {
            updateView.showWaitDialog("正在向服务器验证电话号码是否存在");
        }
        Subscription subscription = repository.checkIfServerTelExists(tel)
                .compose(SchedulersCompat.<UserResponseResult>applyUserResponseSchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<UserResponseResult>() {
                    @Override
                    protected void onError(AppException ex) {
                        UpdatePasswordView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.loadError(UpdatePasswordActivity.REQ_TYPE_CHECK_TEL_EXISTS,ex);
                        }
                    }

                    @Override
                    public void onNext(UserResponseResult userResponseResult) {
                        UpdatePasswordView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            if(userResponseResult.getStatusCode() == 200) {
                                iView.handlerResult(UpdatePasswordActivity.REQ_TYPE_CHECK_TEL_EXISTS,true);
                            } else {
                                iView.handlerResult(UpdatePasswordActivity.REQ_TYPE_CHECK_TEL_EXISTS,false);
                            }

                        }
                    }
                });

        compositeSubscription.add(subscription);;

    }

    /**
     * 修改用户密码
     * @param userTel
     * @param passwd
     */
    public void updatePassword(String userTel,String passwd,int type) {

        showViewLoading();

        Observable<UserResponseResult> observable = null;
        if(UpdatePasswordActivity.PAGE_TYPE_UPDATE_PASSWORD == type) {
            observable = repository.updatePasswd(userTel,passwd);
        } else {
            observable = repository.regetPasswd(userTel,passwd);
        }

        Subscription subscription = observable.compose(SchedulersCompat.<UserResponseResult>applyUserResponseSchedulers())
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
                        UpdatePasswordView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(UserResponseResult userResponseResult) {
                        UpdatePasswordView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.handlerResult(UpdatePasswordActivity.REQ_TYPE_UPDATE_PASSWORD,true);
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }



}
