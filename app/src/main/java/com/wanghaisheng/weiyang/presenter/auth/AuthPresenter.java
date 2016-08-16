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
 * Created by sheng on 2016/5/21.
 */
public class AuthPresenter extends BaseLoadablePresenter<AuthView> {

    @Inject
    UserRepository repository;
    @Inject
    UserStorage userStorage;

    @Inject
    public AuthPresenter() {}

    /**
     * 用户注册
     * @param userTel
     * @param passwd
     */
    public void register(String userTel,String passwd) {
        LogUtils.d("presenter register...");

        handleCommonResult(repository.register(userTel,passwd));
    }

    /**
     * 用户注册
     * @param tel
     * @param passwd
     */
    public void login(String tel,String passwd) {
        handleCommonResult(repository.login(tel,passwd));
    }

    /**
     * 状态码：'status_no' => 800, status_msg' => '操作成功'
     * 状态码：'status_no' => 804, status_msg' => '找不到对应的用户'
     * 状态码：'status_no' => 801, status_msg' => '已存在相应的字段'
     * 状态码：'status_no' => 802, status_msg' => '数据输入有误'
     * 状态码：'status_no' => 803, status_msg' => '服务器操作失败'
     *
     */
    /**
     * 处理相同的
     * @param resultObservable
     */
    public void handleCommonResult(Observable<UserResponseResult> resultObservable) {
        showViewLoading();

        Subscription subscription = resultObservable
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
                        LogUtils.d(ex);
                        AuthView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            LogUtils.d(ex);
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
