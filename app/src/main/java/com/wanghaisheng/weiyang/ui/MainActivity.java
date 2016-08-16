package com.wanghaisheng.weiyang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.AppManager;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.template_lib.widget.ColorIconWithText;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.common.Constants;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.ui.article.BaseMeishiArticleFragment;
import com.wanghaisheng.weiyang.ui.caipu.BaseMeishiCaipuFragment;
import com.wanghaisheng.weiyang.ui.common.NoScrollViewPager;
import com.wanghaisheng.weiyang.ui.my.MeFragment;
import com.wanghaisheng.weiyang.ui.poi.MeishiMapFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.ll_bottom_container)
    LinearLayout bottomContainer;
    @Bind(R.id.main_viewpager)
    NoScrollViewPager mPager;//自定义ViewPager禁止最外层的ViewPager滑动
    @Bind(R.id.tab_index)
    ColorIconWithText tabIndex;
    @Bind(R.id.tab_zhuanti)
    ColorIconWithText tabZhuanti;
    @Bind(R.id.tab_xingqu)
    ColorIconWithText tabXingqu;
    @Bind(R.id.tab_me)
    ColorIconWithText tabMe;
    @Bind(R.id.container)
    View container;
    FragmentStatePagerAdapter mAdapter;

    private List<ColorIconWithText> mTabIndicators = new ArrayList<>();

    private static final int INDEX_ONE = 0;
    private static final int INDEX_TWO = 1;
    private static final int INDEX_THREE = 2;
    private static final int INDEX_FOUR = 3;

    @Override
    protected void onBeforeInitData() {
        super.onBeforeInitData();
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        mTabIndicators.add(tabIndex);
        mTabIndicators.add(tabXingqu);
        mTabIndicators.add(tabZhuanti);
        mTabIndicators.add(tabMe);

        //默认第一个tab为选中状态
        tabIndex.setIconAlpha(1.0f);
    }

    @Override
    public void initData() {
        /*//友盟统计
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.setDebugMode(true);
                MobclickAgent.openActivityDurationTrack(false);
                MobclickAgent.setScenarioType(mAppContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
            }
        });
*/
        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = BaseMeishiCaipuFragment.newInstance();
                        break;
                    case 1:
                        fragment = BaseMeishiArticleFragment.newInstance();
                        break;
                    case 2:
                        fragment = MeishiMapFragment.newInstance();
                        break;
                    case 3:
                        fragment = MeFragment.newInstance();
                        break;
                }

                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }

        };
        //禁止最外层的ViewPager滑动
        mPager.setNoScroll(true);
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currentFragment = mAdapter.getItem(mPager.getCurrentItem());
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @OnClick({R.id.tab_index,R.id.tab_zhuanti,R.id.tab_xingqu,R.id.tab_me})
    public void onClick(View v) {
        //重置所有的Tab的alpha值为0
        resetTabAlpha();

        switch (v.getId()) {
            case R.id.tab_index:
                mTabIndicators.get(INDEX_ONE).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_ONE,false);
                break;
            case R.id.tab_xingqu:
                mTabIndicators.get(INDEX_TWO).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_TWO,false);
                break;
            case R.id.tab_zhuanti:
                mTabIndicators.get(INDEX_THREE).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_THREE,false);
                break;
            case R.id.tab_me:
                mTabIndicators.get(INDEX_FOUR).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_FOUR,false);
                break;
        }
    }

    /**
     * 重置所有的Tab的alpha值为0
     */
    private void resetTabAlpha() {
        for(int i=0; i<mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.d("TAG","position==>"+position+" positionOffset==>"+positionOffset);
        if(positionOffset>0) {
            ColorIconWithText left = mTabIndicators.get(position);
            ColorIconWithText right = mTabIndicators.get(position+1);
            left.setIconAlpha(1-positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
//        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        LogUtils.d("activity onresume ");
        super.onResume();

//        MobclickAgent.onResume(mAppContext);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(mAppContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MobclickAgent.onKillProcess(getApplicationContext());
        AppManager.AppExit(getApplicationContext());
    }

    @Override
    protected void initInjector() {

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(AppContext.getInstance().getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void onBackPressed() {
        if(canExit()) {
            super.onBackPressed();
        }
//        moveTaskToBack(true);
    }

    private long lastPressTime = 0;
    private boolean canExit() {
        if(System.currentTimeMillis() - lastPressTime > Constants.exitConfirmTime) {
            lastPressTime = System.currentTimeMillis();
            ToastUtil.showSnackbar(getString(R.string.notify_exit_confirm),container);
            return false;
        }
        return true;
    }

    public View getBottomContainer() {
        return bottomContainer;
    }
}
