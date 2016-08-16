package com.wanghaisheng.weiyang.presenter.common;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;

import java.util.List;


/**
 * Created by sheng on 2016/6/24.
 */
public interface CollectionListView extends BaseLoadableView {

    void renderCollectionData(List<BaseBean> datas);

}
