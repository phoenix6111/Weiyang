package com.wanghaisheng.weiyang.injector.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.wanghaisheng.weiyang.injector.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Author: sheng on 2016/8/7 21:25
 * Email: 1392100700@qq.com
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return fragment.getActivity();
    }

}
