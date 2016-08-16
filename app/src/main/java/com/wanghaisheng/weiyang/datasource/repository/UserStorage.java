package com.wanghaisheng.weiyang.datasource.repository;

import android.content.Context;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.okhttp.cookie.store.PersistentCookieStore;
import com.wanghaisheng.template_lib.utils.AppConfig;
import com.wanghaisheng.template_lib.utils.PropsUtil;
import com.wanghaisheng.template_lib.utils.StringUtils;
import com.wanghaisheng.weiyang.datasource.beans.User;

import java.util.Properties;


/**
 * Created by sheng on 2016/5/22.
 */
public class UserStorage {

    private PropsUtil propsUtil;
    private Context mContext;
    private PersistentCookieStore cookieStore;

    private int loginUid;

    private boolean login;

    public UserStorage(Context mContext, PropsUtil propsUtil, PersistentCookieStore cookieStore) {
        this.mContext = mContext;
        this.propsUtil = propsUtil;
        this.cookieStore = cookieStore;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {

        this.loginUid = user.getId();
        this.login = true;
        propsUtil.set(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.token",user.getApp_token());
                setProperty("user.name", user.getNick_name());

                LogUtils.d("null avatar  "+(null==user.getAvatar()));

                if(TextUtils.isEmpty(user.getAvatar())) {
                    user.setAvatar("");
                }
                LogUtils.d(user);
                setProperty("user.avatar", user.getAvatar());// 用户头像-文件名
                setProperty("user.gender", user.getGender());
                setProperty("user.tel", user.getTel()+"");
                if(TextUtils.isEmpty(user.getWechat_name())) {
                    user.setWechat_name("");
                }
                setProperty("user.wechat_name", user.getWechat_name());//
                if (TextUtils.isEmpty(user.getWechat_img())) {
                    user.setWechat_img("");
                }
                setProperty("user.wechat_img", user.getWechat_img());//
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        propsUtil.set(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.token",user.getApp_token());
                setProperty("user.name", user.getNick_name());
                if(TextUtils.isEmpty(user.getAvatar())) {
                    user.setAvatar("");
                }
                setProperty("user.avatar", user.getAvatar());// 用户头像-文件名
                setProperty("user.gender", user.getGender());

                setProperty("user.tel", user.getTel()+"");
                if(TextUtils.isEmpty(user.getWechat_name())) {
                    user.setWechat_name("");
                }
                setProperty("user.wechat_name", user.getWechat_name());//
                if (TextUtils.isEmpty(user.getWechat_img())) {
                    user.setWechat_img("");
                }
                setProperty("user.wechat_img", user.getWechat_img());//
                setProperty("user.wechat_name", user.getWechat_name());//
                setProperty("user.wechat_img", user.getWechat_img());//
            }
       });
    }

    /**
     * 异步加载用户信息
     */
    public void initLogin() {
        User user = getLoginUser();
        LogUtils.d(user);
        if (null != user && user.getId() > 0) {
            LogUtils.d("init login...user logined");
            login = true;
            loginUid = user.getId();
        } else {
            cleanLoginInfo();
        }
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(propsUtil.get("user.uid"), 0));
        user.setNick_name(propsUtil.get("user.name"));
        user.setApp_token(propsUtil.get("user.token"));
        user.setAvatar(propsUtil.get("user.avatar"));
        user.setGender(propsUtil.get("user.gender"));
        user.setTel(propsUtil.get("user.tel"));
        user.setWechat_name(propsUtil.get("user.wechat_name"));
        user.setWechat_img(propsUtil.get("user.wechat_img"));

        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        propsUtil.remove("user.uid", "user.name", "user.token", "user.avatar",
                "user.gender", "user.tel", "user.wechat_name", "user.wechat_img");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void logout() {
        cleanLoginInfo();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;
        cookieStore.removeAll();
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        propsUtil.remove(AppConfig.CONF_COOKIE);
    }

}
