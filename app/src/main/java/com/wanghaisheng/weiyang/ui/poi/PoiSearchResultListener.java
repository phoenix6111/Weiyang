package com.wanghaisheng.weiyang.ui.poi;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * Author: sheng on 2016/8/15 13:59
 * Email: 1392100700@qq.com
 * 当PoiSearch返回结果时，回调该接口
 */
public interface PoiSearchResultListener {
    void renderPoiSearchResult(List<PoiItem> poiItems);
}
