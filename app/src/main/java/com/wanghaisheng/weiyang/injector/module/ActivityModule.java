package com.wanghaisheng.weiyang.injector.module;

import android.app.Activity;

import com.wanghaisheng.weiyang.injector.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Author: sheng on 2016/8/7 21:25
 * Email: 1392100700@qq.com
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return activity;
    }

}
