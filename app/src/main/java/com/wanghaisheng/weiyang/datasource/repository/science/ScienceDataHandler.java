package com.wanghaisheng.weiyang.datasource.repository.science;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.beans.ScienceArticleResult;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.EntitiesToArticles;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/7/1.
 */
public class ScienceDataHandler implements IDataHandler,ArticleBeanDataHandler {

    /*public static final String BASE_URL = "http://www.kexuelife.com/";
    public static final int LIMIT = 25;
    public static final String retrieve_TYPE = "by_tag";

    public static final String[] TAG_IDS = {"0","12","9","10","58"
            ,"59","60","61","62","64","65","66","17","19","23","18","40","38","41","46","55","49","28","27","26","29"};


    public static final String[] TAG_NAMES = {"最新内容","新技术","新能源","新材料","智能潮物","可穿戴设备"
            ,"互联网","智能应用","大数据应用","营养饮食","食品安全","农产品","健身保健","医疗卫生"
            ,"两性健康","着装美颜","防灾减灾","节能环保","家居装修","旅游生活","交通工具","交通安全"
            ,"科学摄影","科普文学","科幻作品","生活艺术"};*/

    public static final int LIMIT = 25;
    public static final String retrieve_TYPE = "by_tag";

    ScienceArticleApi scienceArticleApi;

    public ScienceDataHandler(ScienceArticleApi scienceArticleApi) {
        this.scienceArticleApi = scienceArticleApi;
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {

        int offset = getOffset(page);

        Observable<ScienceArticleResult> observable = null;
        if("0".equals(tag)) {
            observable = scienceArticleApi.getIndexScienceArticleResult(offset,LIMIT);
        } else {
            observable = scienceArticleApi.getArticleBeanList(retrieve_TYPE,tag,offset,LIMIT);
        }

        return observable.map(new Func1<ScienceArticleResult, List<BaseBean>>() {
            @Override
            public List<BaseBean> call(ScienceArticleResult scienceArticleResult) {
                return EntitiesToArticles.scienceArticlesToArticleBeans(scienceArticleResult.getResult());
            }
        });
    }

    private int getOffset(int page) {
        if (page == 0) {
            return 0;
        }

        return (page-1)* LIMIT;
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.SCIENCEARTICLE_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.Long_CACHE_TIME;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {

        String url = ((MeishiBean)baseBean).getArticleUrl();

        return scienceArticleApi.getArticleBeanDetail(url).subscribeOn(Schedulers.io());
    }
}
