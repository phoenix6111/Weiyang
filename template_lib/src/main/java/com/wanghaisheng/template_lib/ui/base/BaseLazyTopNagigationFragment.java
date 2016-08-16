package com.wanghaisheng.template_lib.ui.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.appexception.AppException;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseLazyTopNagigationFragment extends BaseFragment {

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

    //lazy执行的方法，也就是说等initData中的方法执行完再执行的方法
    protected void lazyInitView() {

        initTabButton(parentView);

        ViewStub viewStub = (ViewStub) parentView.findViewById(R.id.viewstub);
        viewStub.inflate();
        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        LogUtils.d("index top navigation fragment lazy init view");
        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

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
        } else {
            btnClose.setVisibility(View.GONE);
        }
        btnMore = (ImageView) parentView.findViewById(R.id.btn_more);
        if(showMoreBtn) {
            btnMore.setVisibility(View.VISIBLE);
        } else {
            btnMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if(getChildFragmentManager().getFragments()!=null) {
            getChildFragmentManager().getFragments().clear();
        }*/
    }

    public void loadError(int loadType, AppException e) {
//        toastUtil.showCenterToast(ExceptionMsgFactory.getExceptionMsg(e));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment currentFragment = pagerAdapter.getItem(viewPager.getCurrentItem());
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }


}
