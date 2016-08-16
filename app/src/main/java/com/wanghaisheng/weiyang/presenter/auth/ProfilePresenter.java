package com.wanghaisheng.weiyang.presenter.auth;

import android.net.Uri;

import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadablePresenter;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;
import com.wanghaisheng.weiyang.datasource.repository.SchedulersCompat;
import com.wanghaisheng.weiyang.datasource.repository.UserStorage;
import com.wanghaisheng.weiyang.datasource.repository.user.UserRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/5/24.
 */
public class ProfilePresenter extends BaseLoadablePresenter<ProfileView> {
    @Inject
    UserRepository repository;
    @Inject
    UserStorage userStorage;

    @Inject
    public ProfilePresenter(){}

    /**
     * 修改用户名
     * @param nickname
     */
    public void updateNickname(String nickname) {
        handleCommonResult(ProfileView.UPDATE_TYPE_TYPE_UPDATE_NICKNAME,repository.updateNickname(nickname));
    }

    /**
     * 修改性别
     * @param gender
     */
    public void updateGender(String gender) {
        handleCommonResult(ProfileView.UPDATE_TYPE_TYPE_UPDATE_GENDER,repository.updateGender(gender));
    }

    /**
     * 更新用户头像
     * @param uri
     */
    public void updateAvatar(Uri uri) {
        handleCommonResult(ProfileView.UPDATE_TYPE_TYPE_UPDATE_AVATAR,repository.updateAvatar(uri));
    }

    /**
     * 处理相同的
     * @param resultObservable
     */
    public void handleCommonResult(final int type, Observable<UserResponseResult> resultObservable) {
        showViewLoading();

        Subscription subscription = resultObservable
                .compose(SchedulersCompat.<UserResponseResult>applyUserResponseSchedulers())
                .doOnNext(new Action1<UserResponseResult>() {
                    @Override
                    public void call(UserResponseResult result) {
                        userStorage.saveUserInfo(result.getResult());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<UserResponseResult>() {
                    @Override
                    protected void onError(AppException ex) {
                        ProfileView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.loadError(type,ex);
                        }
                    }

                    @Override
                    public void onNext(UserResponseResult userResponseResult) {
                        ProfileView iView = getView();
                        if(iView != null) {
                            hideViewLoading();
                            iView.updateSuccess(type,userResponseResult.getResult());
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }
}
