package com.wanghaisheng.weiyang.presenter.amap;

import com.wanghaisheng.template_lib.presenter.common.CommonListView;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;

import java.util.List;

/**
 * Author: sheng on 2016/8/15 20:05
 * Email: 1392100700@qq.com
 */
public interface AMapPoiListView extends CommonListView{

    void renderPoiListData(List<MapPoiBean> datas);

}
