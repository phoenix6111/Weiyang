package com.wanghaisheng.weiyang.datasource.repository.jianshu;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;

import java.util.List;

import rx.Observable;


/**
 * Created by sheng on 2016/7/1.
 */
public class JianshuDataHandler implements IDataHandler,ArticleBeanDataHandler {

    public static final String ORDER_BY = "added_at";

    /*public static final String COLLECTION_JIANWENSHIJI = "collections/47/notes?order_by=added_at&page=%d";//简文市集
    public static final String COLLECTION_DUANZIGOU = "collections/12304/notes?order_by=added_at&page=%d";//段子狗
    public static final String COLLECTION_SHENGHUOJIA = "collections/25920/notes?order_by=added_at&page=%d";//生活家
    public static final String COLLECTION_GONJUPI = "collections/32/notes?order_by=added_at&page=%d";//工具癖
    public static final String COLLECTION_TANQINSHUOAI = "collections/49/notes?order_by=added_at&page=%d";//谈情说爱
    public static final String COLLECTION_LISHI = "collections/75/notes?order_by=added_at&page=%d";//历史
    public static final String COLLECTION_SHIJIANSHI = "collections/95/notes?order_by=added_at&page=%d";//世间事
    public static final String COLLECTION_GITHUB = "collections/148/notes?order_by=added_at&page=%d";//GITHUB
    public static final String COLLECTION_QINGCHUN = "collections/20/notes?order_by=added_at&page=%d";//爱情
    public static final String COLLECTION_lianzai = "collections/61/notes?order_by=added_at&page=10";//连载
    public static final String COLLECTION_zuihoutamenmeizaiyiqi = "collections/383/notes?order_by=added_at&page=3";//最后她们没在一起
    public static final String COLLECTION_dazahui = "collections/23781/notes?order_by=added_at&page=13";//大杂烩

    public static final String[] CHANNEL_TAG = {"jianwenshiji","duanzigou","shenghuojia","gonjupi","tanqinshuoai"
            ,"lishi","shijianshi","github","qingchun"};
    public static final String[] CHANNEL_NAME = {"简文市集","段子狗","生活家","工具癖","谈情说爱"
            ,"历史","世间事","GITHUB","青春"};
*/
    JianshuApi jianshuApi;

    public JianshuDataHandler(JianshuApi jianshuApi) {
        this.jianshuApi = jianshuApi;
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {
        return jianshuApi.getArticleBeanList(tag,ORDER_BY,page);
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.JIANSHU_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.SHORT_CACHE_TIME;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        String url = ((MeishiBean)baseBean).getArticleUrl();

        return jianshuApi.getArticleBeanDetail(url);
    }
}
