package com.wanghaisheng.weiyang.datasource.repository.user;

import android.net.Uri;

import com.wanghaisheng.template_lib.datasource.beans.BaseResponseResult;
import com.wanghaisheng.weiyang.datasource.beans.TokenResult;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;

import rx.Observable;


/**
 * Created by sheng on 2016/5/29.
 */
public interface UserRepository {

    /**
     * 获取服务器端token
     * @return
     */
    public Observable<TokenResult> getTokenResult();

    public Observable<UserResponseResult> checkIfServerTelExists(String tel);

    /**
     * 用户注册
     * @param userTel
     * @param passwd
     * @return
     */
    public Observable<UserResponseResult> register(String userTel, String passwd);

    /**
     * 修改用户密码
     * @param userTel
     * @param passwd
     * @param token
     * @return
     */
    public Observable<UserResponseResult> updatePasswd(String userTel, String passwd);

    /**
     * 找回用户密码
     * @param userTel
     * @param passwd
     * @return
     */
    Observable<UserResponseResult> regetPasswd(String userTel, String passwd);

    /**
     * 用户登陆
     * @param userTel
     * @param passwd
     * @return
     */
    public Observable<UserResponseResult> login(String userTel, String passwd);

    /**
     * 更新nickname
     * @param nickname
     * @return
     */
    public Observable<UserResponseResult> updateNickname(String nickname);

    /**
     * 更新性别
     * @param gender
     * @return
     */
    public Observable<UserResponseResult> updateGender(String gender);

    /**
     * 更新用户头像
     * @param avatarUri
     * @return
     */
    public Observable<UserResponseResult> updateAvatar(Uri avatarUri);

    Observable<BaseResponseResult> postFeedback(String title, String content, String img);

}
