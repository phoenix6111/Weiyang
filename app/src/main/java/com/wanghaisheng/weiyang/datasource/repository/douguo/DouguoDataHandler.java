package com.wanghaisheng.weiyang.datasource.repository.douguo;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.ApiFactory;
import com.wanghaisheng.weiyang.datasource.repository.ArticleBeanDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.IDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.converter.DouguoContentParser;
import com.wanghaisheng.weiyang.datasource.repository.converter.IContentParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/7/4.
 */
public class DouguoDataHandler implements IDataHandler,ArticleBeanDataHandler {
    public static final int LIMIT = 30;
    public static final int SEARCH_LIMIT = 20;

    DouguoApi douguoApi;

    public DouguoDataHandler(DouguoApi douguoApi) {
        this.douguoApi = douguoApi;
    }

    @Override
    public Observable<List<BaseBean>> getBaseBeanList(String category, String tag, int page) {
        if("caipu".equals(category)) {
            if("index".equals(tag)) {
                return douguoApi.getIndexBaseBeanList(getCaipuOffset(page));
            }
            tag = tag.replace("_","-");
            return douguoApi.getCaipuBeanList(tag, getCaipuOffset(page));
        } else if("search".equals(category)){
            return douguoApi.searchBaseBeanList(tag, getSearchOffset(page));
        }

        return null;

    }

    private int getCaipuOffset(int page) {
        return LIMIT * (page -1);
    }

    private int getSearchOffset(int page) {
        return SEARCH_LIMIT * (page -1);
    }

    @Override
    public String getCachePrefix(String module) {
        return ApiFactory.DOUGUO_BASE_URL;
    }

    @Override
    public int getCacheTime() {
        return CacheHelper.Long_CACHE_TIME;
    }

    @Override
    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        MeishiBean articleBean = (MeishiBean) baseBean;
        String detailUrlTemplate = "http://m.douguo.com/cookbook/%s.html";
        final String distUrl = String.format(detailUrlTemplate,articleBean.getArticleId());

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                OkHttpUtils.get().url(distUrl).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onResponse(String response) {
                                IContentParser contentParser = new DouguoContentParser();
                                String resStr = contentParser.parseArticleBeanDetail(response);

                                subscriber.onNext(resStr);
                                subscriber.onCompleted();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io());


    }
}
