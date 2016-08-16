package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

/**
 * Created by sheng on 2016/6/17.
 * html内容解析接口，策略模式
 */
public interface IContentParser {

    /**
     * 根据源html，解析成 List
     * @param sourceHtml
     * @return
     */
    List<BaseBean> parseArticleBeanList(String sourceHtml);

    /**
     * 根据源html，解析成 符合要求的 目标html
     * @param sourceHtml
     * @return
     */
    String parseArticleBeanDetail(String sourceHtml);

}
