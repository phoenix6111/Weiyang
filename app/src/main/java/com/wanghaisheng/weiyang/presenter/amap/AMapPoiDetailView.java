package com.wanghaisheng.weiyang.presenter.amap;

import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;

/**
 * Author: sheng on 2016/8/16 21:08
 * Email: 1392100700@qq.com
 */
public interface AMapPoiDetailView extends BaseLoadableView {

    void renderAMapPoiDetail(MapPoiDetailBean detailBean);

}
