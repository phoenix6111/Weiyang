package com.wanghaisheng.weiyang.injector.module;

import android.content.Context;

import com.wanghaisheng.template_lib.utils.ConfigHelper;
import com.wanghaisheng.template_lib.utils.FileHelper;
import com.wanghaisheng.template_lib.utils.FormatHelper;
import com.wanghaisheng.template_lib.utils.NetWorkHelper;
import com.wanghaisheng.template_lib.utils.PrefsUtil;
import com.wanghaisheng.template_lib.utils.PropsUtil;
import com.wanghaisheng.template_lib.utils.RequestHelper;
import com.wanghaisheng.template_lib.utils.ResourceHelper;
import com.wanghaisheng.template_lib.utils.SecurityHelper;
import com.wanghaisheng.template_lib.utils.SettingPrefHelper;
import com.wanghaisheng.template_lib.utils.StringHelper;
import com.wanghaisheng.template_lib.utils.ToastHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by sheng on 2016/4/14.
 */

@Module
public class HelperModule {
    @Provides
    @Singleton
    FormatHelper provideFormatHelper() {
        return new FormatHelper();
    }

    @Provides
    @Singleton
    FileHelper provideFileHelper(Context context) {
        return new FileHelper(context);
    }

    @Provides
    @Singleton
    SecurityHelper provideSecurityHelper() {
        return new SecurityHelper();
    }


    @Provides
    @Singleton
    NetWorkHelper provideNetWorkHelper(Context mContext) {
        return new NetWorkHelper(mContext);
    }


    @Provides
    @Singleton
    RequestHelper provideRequestHelper(SecurityHelper securityHelper, Context context, SettingPrefHelper mSettingPrefHelper) {
        return new RequestHelper(securityHelper, context, mSettingPrefHelper);
    }

    @Provides
    @Singleton
    SettingPrefHelper provideSettingPrefHelper(Context context) {
        return new SettingPrefHelper(context);
    }

    @Provides
    @Singleton
    ResourceHelper provideResourceHelper() {
        return new ResourceHelper();
    }

    @Provides
    @Singleton
    ConfigHelper provideConfigHelper(SettingPrefHelper mSettingPrefHelper) {
        return new ConfigHelper(mSettingPrefHelper);
    }

    @Provides
    @Singleton
    ToastHelper provideToastHelper(Context mContext) {
        return new ToastHelper(mContext);
    }

    @Provides
    @Singleton
    StringHelper provideStringHelper(Context mContext, ToastHelper mToastHelper) {
        return new StringHelper(mContext, mToastHelper);
    }

    @Provides
    @Singleton
    PropsUtil providePropsUtil(Context context) {
        return new PropsUtil(context);
    }

    @Provides
    @Singleton
    PrefsUtil providePrefsUtil(Context context) {
        return new PrefsUtil(context);
    }

    /*@Provides
    @Singleton
    UserStorage provideUserStorage(Context context, PropsUtil propsUtil, PersistentCookieStore cookieStore) {
        return new UserStorage(context,propsUtil,cookieStore);
    }*/


}
