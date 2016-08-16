package com.wanghaisheng.template_lib.presenter.base;

/**
 * Created by sheng on 2016/4/14.
 */
public interface BaseView {
    int ERROR_TYPE_NETWORK = 1;
    int ERROR_TYPE_NODATA = 2;
    int ERROR_TYPE_NODATA_ENABLE_CLICK = 3;
    int ERROR_TYPE_UNKNOWN = 4;

    void error(int errorType, String errMsg);
}
