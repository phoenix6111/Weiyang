package com.wanghaisheng.weiyang.datasource.repository.ttyy;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.EntitiesToArticles;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.converter.TTYYContentParser;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Author: sheng on 2016/8/9 10:41
 * Email: 1392100700@qq.com
 */
public class TTYYDataHandler implements IDataHandler,ArticleBeanDataHandler {

    private TTYYApi ttyyApi;

    public TTYYDataHandler(TTYYApi ttyyApi) {
        this.ttyyApi = ttyyApi;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        final MeishiBean meishiBean = (MeishiBean) baseBean;
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                TTYYContentParser.parseArticleBeanDetail(meishiBean);
            }
        });
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {
        return ttyyApi.getArticleBeanList(page,"ajax")
                .map(new Func1<TTYYBeanResult, List<BaseBean>>() {
                    @Override
                    public List<BaseBean> call(TTYYBeanResult ttyyBeanResult) {
                        if(!ListUtils.isEmpty(ttyyBeanResult.getDataList())) {
                            return EntitiesToArticles.ttyyArticlesToArticleBeans(ttyyBeanResult);
                        }

                        return new ArrayList<>();
                    }
                });
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.TTYY_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.Long_CACHE_TIME;
    }
}
