package com.wanghaisheng.weiyang.datasource.repository.wechatarticle;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;

import java.util.List;

import rx.Observable;


/**
 * Created by sheng on 2016/7/1.
 */
public class WechatarticleDataHandler implements IDataHandler {

    public static final String KEY_SEARCH_TAG = "wechatarticle_search_tag";
    /*
    //    public static final String BASE_URL = "http://weixin.sogou.com/";
    public static final int SEARCH_TYPE = 2;

    public static final String[] CHANNEL_TAGS = {"wap_0","wap_1","wap_2","wap_3","wap_4","wap_5",
            "wap_6","wap_7","wap_8","wap_9","wap_10",
            "wap_11","wap_12","wap_13","wap_14","wap_15","wap_16",
            "wap_17","wap_18","wap_19","lenyazhuanlan"
    };

    public static final String[] CHANNEL_TITLES = {"热门","推荐","段子手","养生堂","私房话",
            "八卦精","百事通","财经迷","汽车迷","科技咖",
            "万人迷","宝宝控","点赞党","旅行家","白骨精",
            "美食家","古今通","考证党","星座控","体育迷"};*/

    public static final int SEARCH_TYPE = 2;
    private WechatArticleApi wechatArticleApi;
    public static final String CATEGORY_SEARCH = "search";

    public WechatarticleDataHandler(WechatArticleApi wechatArticleApi) {
        this.wechatArticleApi = wechatArticleApi;
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {
        if(CATEGORY_SEARCH.equals(category)) {
            return wechatArticleApi.searchWechatArticleList(page,tag,SEARCH_TYPE);
        }

        return wechatArticleApi.getArticleBeanList(tag,page);
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.WECHATARTICLE_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.SHORT_CACHE_TIME;
    }

}
