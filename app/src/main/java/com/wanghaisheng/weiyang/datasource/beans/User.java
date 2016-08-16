package com.wanghaisheng.weiyang.datasource.beans;

import java.io.Serializable;

/**
 * Created by sheng on 2016/5/21.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -3663986777214160944L;
    private int id;//用户ID
    private String app_token;
    private String avatar;//用户头像
    private String nick_name;//用户昵称
    private String gender;//用户性别
    private String tel;//用户电话号码
    private String wechat_name;//用户微信昵称
    private String wechat_img;//用户微信头像

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWechat_name() {
        return wechat_name;
    }

    public void setWechat_name(String wechat_name) {
        this.wechat_name = wechat_name;
    }

    public String getWechat_img() {
        return wechat_img;
    }

    public void setWechat_img(String wechat_img) {
        this.wechat_img = wechat_img;
    }
}
