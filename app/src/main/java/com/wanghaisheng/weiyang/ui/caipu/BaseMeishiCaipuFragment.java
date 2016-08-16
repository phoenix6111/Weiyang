package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wanghaisheng.template_lib.ui.base.BaseLazyTopNagigationFragment;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.common.ChannelPresenter;
import com.wanghaisheng.weiyang.presenter.common.ChannelView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sheng on 2016/7/4.
 */
public class BaseMeishiCaipuFragment extends BaseLazyTopNagigationFragment implements ChannelView{

    @Inject
    ChannelPresenter presenter;

    List<ZhuantiChannelEntity> channelEntities;

    public static BaseMeishiCaipuFragment newInstance() {
        return new BaseMeishiCaipuFragment();
    }

    @Override
    protected FragmentStatePagerAdapter initPagerAdapter() {

        return new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MeishiListFragment.newInstance(channelEntities.get(position));
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
            presenter.getSyncChannelEntities(ModuleConstants.MEISHI_MODULE_CAIPU);
        }

        showCloseBtn = false;
        showMoreBtn = true;
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
//        LogUtils.d("render saved channel bean");
//        LogUtils.d(channelEntities);
        lazyInitView();

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.openMeishiCaipuActivity(getActivity());
            }
        });
    }
}