package com.wanghaisheng.template_lib.presenter.base;

/**
 * Created by sheng on 2016/5/31.
 */
public interface BaseLoadableView extends IView{
    int ERROR_TYPE_NETWORK = 0x001;
    int ERROR_TYPE_NODATA = 0x002;
    int ERROR_TYPE_PARSE = 0x003;
    int ERROR_TYPE_PERMISSION = 0x004;


    /**
     * 隐藏loading
     */
    void hideLoading();

    /**
     * 显示loading
     */
    void showLoading();

}
