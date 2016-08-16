package com.wanghaisheng.weiyang.navigator;

import android.content.Context;
import android.content.Intent;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.navigator.BaseNavigator;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.ui.MainActivity;
import com.wanghaisheng.weiyang.ui.auth.AuthActivity;
import com.wanghaisheng.weiyang.ui.auth.ProfileActivity;
import com.wanghaisheng.weiyang.ui.auth.UpdatePasswordActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiCaipuDetailActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiHomeActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiSearchActivity;
import com.wanghaisheng.weiyang.ui.caipu.SelectCaipuActivity;
import com.wanghaisheng.weiyang.ui.common.ArticleBeanDetailActivity;
import com.wanghaisheng.weiyang.ui.common.WechatArticleDetailActivity;
import com.wanghaisheng.weiyang.ui.poi.PoiDetailActivity;

/**
 * Author: sheng on 2016/8/8 09:16
 * Email: 1392100700@qq.com
 */
public class Navigator extends BaseNavigator {

    public static void openMainActivity(Context context) {
        start(context, MainActivity.class);
    }

    /**
     * 打开用户登陆或注册activity
     * @param context
     * @param authType
     */
    public static void openAuthActivity(Context context,int authType) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.putExtra(AuthActivity.ARG_AUTH_TYPE,authType);
        start(context,intent);
    }

    public static void openLoginView(Context context) {
        openAuthActivity(context,AuthActivity.AUTH_TYPE_LOGIN);
    }

    public static void openRegisterView(Context context) {
        openAuthActivity(context,AuthActivity.AUTH_TYPE_REGISTER);
    }

    public static void openUpdatePasswordActivity(Context context,int viewType) {
        Intent intent = new Intent(context, UpdatePasswordActivity.class);
        intent.putExtra(UpdatePasswordActivity.ARG_PAGE_TYPE,viewType);
        start(context,intent);
    }

    public static void openUpdatewordView(Context context) {
        openUpdatePasswordActivity(context,UpdatePasswordActivity.PAGE_TYPE_UPDATE_PASSWORD);
    }

    public static void openRegetPasswordView(Context context) {
        openUpdatePasswordActivity(context,UpdatePasswordActivity.PAGE_TYPE_REGET_PASSWORD);
    }

    public static void openProfileActivity(Context context) {
        start(context, ProfileActivity.class);
    }

    /**
     * 打开美食搜索activity
     * @param context
     */
    public static void openMeishiSearchActivity(Context context) {
        start(context, MeishiSearchActivity.class);
    }

    public static void openMeishiCaipuActivity(Context context) {
        start(context,SelectCaipuActivity.class);
    }

    public static void openMeishiHomeActivity(Context context) {
        start(context, MeishiHomeActivity.class);
    }

    public static void openMeishiCaipuDetailActivity(Context context, String caipuName) {
        Intent intent = new Intent(context, MeishiCaipuDetailActivity.class);
        intent.putExtra(MeishiCaipuDetailActivity.ARG_CAIPU,caipuName);
        start(context,intent);
    }

    /**
     * 打开微信搜索activity
     * @param context
     */
    /*public static void openWechatArticleSearchActivity(Context context) {
        start(context, WechatArticleSearchActivity.class);
    }*/

    public static void openWechatArticleDetail(Context context, MeishiBean article) {
        Intent intent = new Intent(context, WechatArticleDetailActivity.class);
        intent.putExtra(WechatArticleDetailActivity.ARG_articlebean,article);

        start(context,intent);
    }

    public static void openArticleBeanDetailActivity(Context context, BaseBean baseBean) {
        Intent intent = new Intent(context, ArticleBeanDetailActivity.class);
        intent.putExtra(ArticleBeanDetailActivity.ARG_ARTICLEBEAN,baseBean);

        start(context,intent);
    }

    public static void openPoiDetailActivity(Context context,MapPoiBean poiBean) {
        Intent intent = new Intent(context, PoiDetailActivity.class);
        intent.putExtra(PoiDetailActivity.POI_ID,poiBean);
        start(context,intent);
    }

}
