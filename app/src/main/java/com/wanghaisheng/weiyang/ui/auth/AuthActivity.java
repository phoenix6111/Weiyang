package com.wanghaisheng.weiyang.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sheng on 2016/6/24.
 */
public class AuthActivity extends BaseActivity {
    private static final String TAG = "AuthActivity";
    
    public static final String ARG_AUTH_TYPE = "arg_auth_type";
    public static final int AUTH_TYPE_LOGIN = 0x000;
    public static final int AUTH_TYPE_REGISTER = 0x001;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_nextaction)
    TextView tvNextaction;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    //类型：LOGIN , REGISTER
    private int authType;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.authType = getIntent().getIntExtra(ARG_AUTH_TYPE,AUTH_TYPE_LOGIN);
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
        return R.layout.act_auth_loginandreg;
    }

    /**
     * 设置toolbar右边textview
     */
    private void setupNextAction() {
        if(AUTH_TYPE_LOGIN == authType) {
            tvNextaction.setText("注册新账号？");
        } else {
            tvNextaction.setText("已有账号？");
        }
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.tabbar_close_icon2);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupNextAction();
        fragmentList.add(LoginFragment.newInstance());
        fragmentList.add(RegisterFragment.newInstance());
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(0 == position) {
                    authType = AUTH_TYPE_LOGIN;
                } else {
                    authType = AUTH_TYPE_REGISTER;
                }

                setupNextAction();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.tv_nextaction})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nextaction:
                LogUtils.d("click  next action....");
                if(AUTH_TYPE_LOGIN == authType) {
                    mViewPager.setCurrentItem(1);
                } else {
                    mViewPager.setCurrentItem(0);
                }
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int currentItem = mViewPager.getCurrentItem();
        Fragment currentFragment = fragmentList.get(currentItem);
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }
}
