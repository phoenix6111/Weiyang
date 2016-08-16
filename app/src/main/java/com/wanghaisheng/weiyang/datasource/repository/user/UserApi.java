package com.wanghaisheng.weiyang.datasource.repository.user;

import com.wanghaisheng.template_lib.datasource.beans.BaseResponseResult;
import com.wanghaisheng.weiyang.datasource.beans.TokenResult;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;


/**
 * Created by sheng on 2016/5/21.
 */
public interface UserApi {

    /**
     * 获取token
     * @return
     */
    @GET("token")
    Observable<TokenResult> getToken();

    /**
     * 用户注册
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    Observable<UserResponseResult> register(@FieldMap Map<String, String> params);

    /**
     * 修改用户密码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("update_passwd")
    Observable<UserResponseResult> updatePasswd(@FieldMap Map<String, String> params);

    /**
     * 找回用户密码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("reget_passwd")
    Observable<UserResponseResult> regetPasswd(@FieldMap Map<String, String> params);

    /**
     * 用户登陆
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<UserResponseResult> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("update_nickname")
    Observable<UserResponseResult> updateNickname(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("update_gender")
    Observable<UserResponseResult> updateGender(@FieldMap Map<String, String> params);

    @Multipart
    @POST("update_avatar")
    Observable<UserResponseResult> updateAvatar(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("check_tel")
    Observable<UserResponseResult> checkServerTelExists(@FieldMap Map<String, String> params);

    @Multipart
    @POST("feedback")
    Observable<BaseResponseResult> postFeedback(@Part MultipartBody.Part photo, @Part("title") RequestBody tel
            , @Part("content") RequestBody content, @Part("client") RequestBody client);
}
