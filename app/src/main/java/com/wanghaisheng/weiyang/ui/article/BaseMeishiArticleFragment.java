package com.wanghaisheng.weiyang.ui.article;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wanghaisheng.template_lib.ui.base.BaseLazyTopNagigationFragment;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.presenter.common.ChannelPresenter;
import com.wanghaisheng.weiyang.presenter.common.ChannelView;

import java.util.List;

import javax.inject.Inject;

/**
 * Author: sheng on 2016/8/8 11:15
 * Email: 1392100700@qq.com
 */
public class BaseMeishiArticleFragment extends BaseLazyTopNagigationFragment implements ChannelView {

    @Inject
    ChannelPresenter presenter;

    List<ZhuantiChannelEntity> channelEntities;

    public static BaseMeishiArticleFragment newInstance() {
        return new BaseMeishiArticleFragment();
    }

    @Override
    protected FragmentStatePagerAdapter initPagerAdapter() {

        return new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MeishiArticleListFragment.newInstance(channelEntities.get(position));
            }

            @Override
            public int getCount() {
                return channelEntities.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return channelEntities.get(position).getChannel_title();
            }
        };
    }

    @Override
    public void beforeInitView(View view,Bundle savedInstanceState) {
        if(presenter != null) {
            presenter.attachView(this);
            presenter.getSyncChannelEntities(ModuleConstants.MEISHI_MODULE_ARTICLE);
        }

        showCloseBtn = false;
        showMoreBtn = false;
        smartTabLayout = (SmartTabLayout) parentView.findViewById(R.id.tab_layout);

    }

    @Override
    public void initInjector() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public void renderSavedChannelBean(List<ZhuantiChannelEntity> channelEntities) {
        this.channelEntities = channelEntities;
        LogUtils.d("render saved channel bean");
        LogUtils.d(channelEntities);
        lazyInitView();

    }
}