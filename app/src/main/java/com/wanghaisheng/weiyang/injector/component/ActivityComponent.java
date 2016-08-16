package com.wanghaisheng.weiyang.injector.component;

import android.app.Activity;

import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.injector.scopes.PerActivity;
import com.wanghaisheng.weiyang.ui.MainActivity;
import com.wanghaisheng.weiyang.ui.auth.AuthActivity;
import com.wanghaisheng.weiyang.ui.auth.ProfileActivity;
import com.wanghaisheng.weiyang.ui.auth.SecurityCodeActivity;
import com.wanghaisheng.weiyang.ui.auth.UpdatePasswordActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiCaipuDetailActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiHomeActivity;
import com.wanghaisheng.weiyang.ui.caipu.MeishiSearchActivity;
import com.wanghaisheng.weiyang.ui.caipu.SelectCaipuActivity;
import com.wanghaisheng.weiyang.ui.common.ArticleBeanDetailActivity;
import com.wanghaisheng.weiyang.ui.common.WechatArticleDetailActivity;
import com.wanghaisheng.weiyang.ui.poi.PoiDetailActivity;

import dagger.Component;
import dagger.Subcomponent;


/**
 * Created by sheng on 2016/4/13.
 */
@PerActivity
@Subcomponent
@Component(modules = {ActivityModule.class},dependencies = AppComponent.class)
public interface ActivityComponent {
    Activity getActivityContext();

    void inject(MainActivity mainActivity);

    void inject(MeishiCaipuDetailActivity meishiCaipuDetailActivity);

    void inject(MeishiHomeActivity meishiHomeActivity);

    void inject(MeishiSearchActivity meishiSearchActivity);

    void inject(SelectCaipuActivity selectCaipuActivity);

    void inject(ArticleBeanDetailActivity articleBeanDetailActivity);

    void inject(WechatArticleDetailActivity wechatArticleDetailActivity);

    void inject(AuthActivity authActivity);

    void inject(ProfileActivity profileActivity);

    void inject(SecurityCodeActivity securityCodeActivity);

    void inject(UpdatePasswordActivity updatePasswordActivity);

    void inject(PoiDetailActivity poiDetailActivity);

}
