package com.wanghaisheng.template_lib.presenter.common;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;

import java.util.List;


/**
 * Created by sheng on 2016/6/15.
 */
public interface CommonListView extends BaseLoadableView {
    void renderData(List<BaseBean> datas);
}
