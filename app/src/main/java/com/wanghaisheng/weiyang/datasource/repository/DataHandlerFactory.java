package com.wanghaisheng.weiyang.datasource.repository;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.datasource.cache.CacheHelper;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoApi;
import com.wanghaisheng.weiyang.datasource.repository.douguo.DouguoDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuApi;
import com.wanghaisheng.weiyang.datasource.repository.jianshu.JianshuDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.science.ScienceDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuApi;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepositoryImpl;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYApi;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatArticleApi;
import com.wanghaisheng.weiyang.datasource.repository.wechatarticle.WechatarticleDataHandler;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinApi;
import com.wanghaisheng.weiyang.datasource.repository.yin.YinDataHandler;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sheng on 2016/7/1.
 */
public class DataHandlerFactory {

    private ICache iCache;

    private WechatArticleApi wechatArticleApi;
    private YinApi yinApi;
    private DouguoApi douguoApi;
    private JianshuApi jianshuApi;
    private ScienceArticleApi scienceArticleApi;
    private SohuApi sohuApi;
    private TTYYApi ttyyApi;

    public DataHandlerFactory(ICache iCache, WechatArticleApi wechatArticleApi, YinApi yinApi ,
             DouguoApi douguoApi,JianshuApi jianshuApi,ScienceArticleApi scienceArticleApi,SohuApi sohuApi,
                              TTYYApi ttyyApi) {
        this.iCache = iCache;
        this.wechatArticleApi = wechatArticleApi;
        this.yinApi = yinApi;
        this.douguoApi = douguoApi;
        this.jianshuApi = jianshuApi;
        this.scienceArticleApi = scienceArticleApi;
        this.sohuApi = sohuApi;
        this.ttyyApi = ttyyApi;
    }


    public IDataHandler getDataHandler(String module) {

        if(ModuleConstants.MODULE_IDENTITY_WECHATARTICLE.equals(module)){
            return new WechatarticleDataHandler(wechatArticleApi);
        }  else if(ModuleConstants.MODULE_IDENTITY_YIN.equals(module)) {
            return new YinDataHandler(yinApi);
        } else if(ModuleConstants.MODULE_IDENTITY_DOUGUO.equals(module)) {
            return new DouguoDataHandler(douguoApi);
        } else if(ModuleConstants.MODULE_IDENTITY_JIANSHU.equals(module)) {
            return new JianshuDataHandler(jianshuApi);
        } else if(ModuleConstants.MODULE_IDENTITY_SCIENCE.equals(module)) {
            return new ScienceDataHandler(scienceArticleApi);
        } else if(ModuleConstants.MODULE_IDENTITY_TTYY.equals(module)) {
            return new TTYYDataHandler(ttyyApi);
        }

        return null;
    }


    /**
     * 先从disk加载，若disk没有，则从网络加载
     * @param moduleIdentity
     * @param page
     * @return
     */
    public Observable<List<BaseBean>> subscribeData(String moduleIdentity, int page) {

        String[] moduleInfos = moduleIdentity.split("\\|");
        String module = moduleInfos[0];
        String category = moduleInfos[1];
        String tag = moduleInfos[2];

        final IDataHandler dataHandler = getDataHandler(module);
        String cachePrefix = dataHandler.getCachePrefix(module);
        final String cacheKey = CacheHelper.getCacheKey(cachePrefix,category,tag,page);

        Observable<List<BaseBean>> diskCache = iCache.getObservableCacheData(cacheKey);

        return Observable.concat(
                diskCache
                ,networkData(moduleIdentity,page))
                .first(new Func1<List<BaseBean>, Boolean>() {
                    @Override
                    public Boolean call(List<BaseBean> datas) {
                        return !ListUtils.isEmpty(datas);
                    }
                });

    }

    /**
     * 从网络加载并保存到 disk
     * @param moduleIdentity
     * @param page
     * @return
     */
    public Observable<List<BaseBean>> networkData(String moduleIdentity, int page) {

        String[] moduleInfos = moduleIdentity.split("\\|");
        String module = moduleInfos[0];
        String category = moduleInfos[1];
        String tag = moduleInfos[2];

        final IDataHandler dataHandler = getDataHandler(module);
        String cachePrefix = dataHandler.getCachePrefix(module);
        final String cacheKey = CacheHelper.getCacheKey(cachePrefix,category,tag,page);
        return dataHandler.getBaseBeanList(category,tag,page)
                .doOnNext(new Action1<List<BaseBean>>() {
                    @Override
                    public void call(final List<BaseBean> baseBeen) {
                        Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                iCache.saveObject(cacheKey, (Serializable) baseBeen,dataHandler.getCacheTime());
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                LogUtils.d(throwable);
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io());

    }

    public Observable<String> getArticleBeanDetail(BaseBean baseBean) {
        ArticleBeanDataHandler dataHandler = getArticleBeanDataHandler(baseBean);

        return dataHandler.getArticleBeanDetail(baseBean);
    }

    private ArticleBeanDataHandler getArticleBeanDataHandler(BaseBean baseBean) {
        String module = baseBean.getModuleName();
        LogUtils.d(baseBean);
        if(ModuleConstants.MODULE_IDENTITY_YIN.equals(module)) {
            return new YinDataHandler(yinApi);
        } else if(ModuleConstants.MODULE_IDENTITY_DOUGUO.equals(module)) {
            return new DouguoDataHandler(douguoApi);
        } else if(ModuleConstants.MODULE_IDENTITY_JIANSHU.equals(module)) {
            return new JianshuDataHandler(jianshuApi);
        } else if(ModuleConstants.MODULE_IDENTITY_SCIENCE.equals(module)) {
            return new ScienceDataHandler(scienceArticleApi);
        } else if(ModuleConstants.MODULE_IDENTITY_SOHU.equals(module)) {
            return new SohuRepositoryImpl(iCache,sohuApi);
        } else if(ModuleConstants.MODULE_IDENTITY_TTYY.equals(module)) {
            return new TTYYDataHandler(ttyyApi);
        }

        return null;
    }

}
