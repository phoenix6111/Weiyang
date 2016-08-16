package com.wanghaisheng.weiyang.presenter.article;

import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;

/**
 * Created by sheng on 2016/6/19.
 */
public interface ArticleBeanDetailView extends BaseLoadableView {
    void renderWebview(String webPageStr);

    void sohuLoadMoreContent(String str);
}
