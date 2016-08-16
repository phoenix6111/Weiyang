package com.wanghaisheng.weiyang.datasource.repository.user;

import android.net.Uri;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.datasource.beans.BaseResponseResult;
import com.wanghaisheng.template_lib.utils.CryptAES;
import com.wanghaisheng.template_lib.utils.SecurityHelper;
import com.wanghaisheng.template_lib.utils.SystemUtils;
import com.wanghaisheng.weiyang.common.Constants;
import com.wanghaisheng.weiyang.datasource.beans.TokenResult;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;


/**
 * Created by sheng on 2016/5/29.
 */
public class UserRepositoryImpl implements UserRepository {

    public static final String ARG_USER_TEL = "tel";
    public static final String ARG_USER_PASSWD = "passwd";
    public static final String ARG_CLIENT = "client";
    public static final String ARG_TOKEN = "X-CSRF-TOKEN";
    public static final String ARG_NICKNAME = "nickname";
    public static final String ARG_GENDER = "gender";
    public static final String ARG_SECRIT = "app_secrit";

    private UserApi userApi;
    public UserRepositoryImpl(UserApi userApi) {
        this.userApi = userApi;
    }

    /**
     * 获取服务器端token
     * @return
     */
    public Observable<TokenResult> getTokenResult() {
        return userApi.getToken();
    }

    @Override
    public Observable<UserResponseResult> checkIfServerTelExists(String tel) {
        Map<String,String> params = new HashMap<>();
        params.put(ARG_USER_TEL,tel);
        String clientId = SystemUtils.getDeviceId();
        params.put(ARG_CLIENT,clientId);
        String app_secrit = CryptAES.AES_Encrypt(Constants.APP_SECRIT,"xiaoya");
        params.put(ARG_SECRIT,app_secrit);

        return userApi.checkServerTelExists(params);
    }

    /**
     * 用户注册
     * @param userTel
     * @param passwd
     * @return
     */
    public Observable<UserResponseResult> register(String userTel, String passwd) {
        String clientId = SystemUtils.getDeviceId();

        Map<String,String> params = new HashMap<>();
        params.put(ARG_USER_TEL,userTel);
        passwd = SecurityHelper.getMD5(passwd);
        params.put(ARG_USER_PASSWD,passwd);
        params.put(ARG_CLIENT,clientId);

        LogUtils.d(params);
        LogUtils.d(clientId);

        return userApi.register(params);
    }

    /**
     * 修改用户密码
     * @param userTel
     * @param passwd
     * @return
     */
    public Observable<UserResponseResult> updatePasswd(String userTel, String passwd) {
        String clientId = SystemUtils.getDeviceId();

        Map<String,String> params = new HashMap<>();
        params.put(ARG_USER_TEL,userTel);
        passwd = SecurityHelper.getMD5(passwd);
        params.put(ARG_USER_PASSWD,passwd);
        params.put(ARG_CLIENT,clientId);

        LogUtils.d(params);
        LogUtils.d(clientId);

        return userApi.updatePasswd(params);
    }

    @Override
    public Observable<UserResponseResult> regetPasswd(String userTel, String passwd) {
        String clientId = SystemUtils.getDeviceId();

        Map<String,String> params = new HashMap<>();
        params.put(ARG_USER_TEL,userTel);
        passwd = SecurityHelper.getMD5(passwd);
        params.put(ARG_USER_PASSWD,passwd);
        params.put(ARG_CLIENT,clientId);
        String app_secrit = CryptAES.AES_Encrypt(Constants.APP_SECRIT,"xiaoya");
        params.put(ARG_SECRIT,app_secrit);

        LogUtils.d(params);
        LogUtils.d(clientId);

        return userApi.regetPasswd(params);
    }

    /**
     * 用户登陆
     * @param userTel
     * @param passwd
     * @return
     */
    public Observable<UserResponseResult> login(String userTel, String passwd) {
        Map<String,String> params = new HashMap<>();
        params.put(ARG_USER_TEL,userTel);
        passwd = SecurityHelper.getMD5(passwd);
        params.put(ARG_USER_PASSWD,passwd);

        return userApi.login(params);
    }

    /**
     * 更新nickname
     * @param nickname
     * @return
     */
    public Observable<UserResponseResult> updateNickname(String nickname) {
        Map<String,String> params = new HashMap<>();
        params.put(ARG_NICKNAME,nickname);

        return userApi.updateNickname(params);
    }

    /**
     * 更新性别
     * @param gender
     * @return
     */
    public Observable<UserResponseResult> updateGender(String gender) {
        LogUtils.d("gender  "+gender);
        Map<String,String> params = new HashMap<>();
        params.put(ARG_GENDER,gender);

        return userApi.updateGender(params);
    }

    /**
     * 更新用户头像
     * @param avatarUri
     * @return
     */
    public Observable<UserResponseResult> updateAvatar(Uri avatarUri) {

        File file = null;
        try {
            file = new File(new URI(avatarUri.toString()));
        } catch (URISyntaxException e) {
            LogUtils.d(e);
        }

        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        Map<String,RequestBody> params = new HashMap<>();
        params.put("avatar\"; filename=\""+file.getName()+"", requestBody);

        return userApi.updateAvatar(params);
    }

    public Observable<BaseResponseResult> postFeedback(String title, String content, String img) {
        LogUtils.d(img);
        File file = new File(img);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("feedback_img", file.getName(), photoRequestBody);

        RequestBody titleReq = RequestBody.create(MediaType.parse("multipart/form-data"),title);
        RequestBody contentReq = RequestBody.create(MediaType.parse("multipart/form-data"),content);
        String client = SystemUtils.getDeviceId();
        RequestBody clientReq = RequestBody.create(MediaType.parse("multipart/form-data"),client);
        return userApi.postFeedback(photo,titleReq,contentReq,clientReq);
    }
}
