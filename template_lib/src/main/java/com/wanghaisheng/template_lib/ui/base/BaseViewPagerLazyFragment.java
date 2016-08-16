package com.wanghaisheng.template_lib.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseViewPagerLazyFragment extends Fragment {

    //标志位，标志已经初始化完成
    protected boolean isPrepared;
    //是否已被加载过一次，第二次就不再去请求数据了
    protected boolean mHasLoadedOnce;
    //Fragment当前状态是否可见
    protected boolean isVisible;

    protected View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this,parentView);
        //在initView之前执行的方法
        beforeInitView(parentView,savedInstanceState);

        initView(parentView,savedInstanceState);
        isPrepared = true;

        return parentView;
    }

    //获取layout文件的id
    public abstract int getLayoutId();

    protected void beforeInitView(View view,Bundle savedInstanceState) {}

    protected void initView(View view,Bundle savedInstanceState) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化dagger2 注入
        initInjector();

        //获取系统保存的savedInstanceState
        getSavedInstanceSatte(savedInstanceState);

        //获取实例化fragment时传进的参数
        Bundle args = getArguments();
        if(null != args) {
            getSavedBundle(args);
        }
    }

    protected abstract void initInjector();

    protected void getSavedInstanceSatte(Bundle savedInstanceState) {}

    protected void getSavedBundle(Bundle args) {}

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected void lazyLoad() {
//        LogUtils.d("return "+"ispreared  "+isPrepared+"  isVisible  "+isVisible);
    }

    /**
     * 可见
     */
    protected void onVisible() {
//        LogUtils.d("ispreared  "+isPrepared+"  isVisible  "+isVisible+"  mHasLoadedonce  "+mHasLoadedOnce);

        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


}