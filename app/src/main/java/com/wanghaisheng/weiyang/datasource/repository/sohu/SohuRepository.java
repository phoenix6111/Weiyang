package com.wanghaisheng.weiyang.datasource.repository.sohu;

import rx.Observable;

/**
 * Author: sheng on 2016/8/10 10:08
 * Email: 1392100700@qq.com
 */
public interface SohuRepository {

    Observable<SohuData> networkArticleBeanResult(String category, String tag, final int page);

    Observable<SohuData> subscribeArticleBean(String category, String tag, final int page);

    /**
     * sohu 加载更我内容
     * @param id
     * @return
     */
    Observable<String> loadMoreContent(String id);

}
