package com.wanghaisheng.weiyang.presenter.common;

import com.wanghaisheng.template_lib.presenter.base.IView;

import java.util.List;

/**
 * Created by sheng on 2016/6/12.
 */
public interface SearchHistoryView extends IView {

    void renderSearchHistoryResult(List<String> datas);

}
