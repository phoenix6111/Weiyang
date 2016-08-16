package com.wanghaisheng.template_lib.ui.base;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wanghaisheng.template_lib.R;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseTopNagigationFragment extends BaseFragment {
    //tab上的关闭按钮
    protected ImageView btnClose;
    //是否显示关闭按钮
    protected boolean showCloseBtn = false;
    //tab上的 ‘更多’按钮
    protected ImageView btnMore;
    //是否显示‘更多’按钮
    protected boolean showMoreBtn;
    protected ViewPager viewPager;
    protected SmartTabLayout smartTabLayout;
    protected FragmentStatePagerAdapter pagerAdapter;

    protected abstract FragmentStatePagerAdapter initPagerAdapter();

    public int getLayoutId() {
        return R.layout.common_tab_navigation;
    }

    public void initView(View parentView){
        initTabButton(parentView);

        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        smartTabLayout = (SmartTabLayout) parentView.findViewById(R.id.tab_layout);
        smartTabLayout.setViewPager(viewPager);
    }

    /**
     * 初始化Tab上的按钮
     * @param parentView
     */
    private void initTabButton(View parentView) {
        btnClose = (ImageView) parentView.findViewById(R.id.btn_close);
        if(showCloseBtn) {
            btnClose.setVisibility(View.VISIBLE);
        }
        btnMore = (ImageView) parentView.findViewById(R.id.btn_more);
        if(showMoreBtn) {
            btnMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null) {
            getChildFragmentManager().getFragments().clear();
        }
    }


}
