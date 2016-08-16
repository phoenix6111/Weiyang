package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;

/**
 * Created by sheng on 2016/7/4.
 */
public class MeishiHomeActivity extends BaseActivity {
    private static final String TAG = "MeishiHomeActivity";

    @Override
    public void getDatas(Bundle savedInstanceState) {

    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((AppContext)getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_content_empty;
    }

    @Override
    public void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, BaseMeishiCaipuFragment.newInstance());
        transaction.commit();

    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(mAppContext);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(mAppContext);
    }
}
