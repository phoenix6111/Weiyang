package com.wanghaisheng.weiyang.datasource.repository.yin;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.beans.YinBeanResult;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.EntitiesToArticles;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by sheng on 2016/7/1.
 */
public class YinDataHandler implements IDataHandler,ArticleBeanDataHandler {
    public static final int PS = 7;
    private YinApi yinApi;

    public YinDataHandler(YinApi yinApi) {
        this.yinApi = yinApi;
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {
        return yinApi.getArticleBeanList(tag,page,PS)
                .map(new Func1<YinBeanResult, List<BaseBean>>() {
                    @Override
                    public List<BaseBean> call(YinBeanResult yinBeanResult) {
                        if("0".equals(yinBeanResult.getResultCode())) {
                            return EntitiesToArticles.yinBeanResultToBaseBeanList(yinBeanResult.getData());
                        }

                        return new ArrayList<BaseBean>();
                    }
                });
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.YIN_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.MIDULE_CACHE_TIME;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        String url = ((MeishiBean)baseBean).getArticleUrl();

        return yinApi.getArticleBeanDetail(url);
    }
}
