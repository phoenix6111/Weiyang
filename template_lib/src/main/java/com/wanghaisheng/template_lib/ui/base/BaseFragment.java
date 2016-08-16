package com.wanghaisheng.template_lib.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by sheng on 2016/4/13.
 *  标准模块父类，重新定义生命周期
 */
public abstract class BaseFragment extends Fragment{
    public boolean isPrepare = false;

    //获取layout文件的id
    public abstract int getLayoutId();

    protected View parentView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //在initView之前执行的方法
        beforeInitView(parentView,savedInstanceState);

        initView(parentView,savedInstanceState);

        initData();

        isPrepare = true;
    }

    protected void initInjector() {}

    /**
     * 在执行initView之前执行的任务
     * @param view
     */
    protected void beforeInitView(View view,Bundle savedInstanceState) {}

    protected void initView(View view,Bundle savedInstanceState) {}

    //初始化数据
    protected void initData(){}

    protected void getSavedBundle(Bundle args) {}

    protected void getSavedInstanceSatte(Bundle savedInstanceState) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();

        getSavedInstanceSatte(savedInstanceState);

        Bundle args = getArguments();
        if(null != args) {
            getSavedBundle(args);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this,parentView);
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
