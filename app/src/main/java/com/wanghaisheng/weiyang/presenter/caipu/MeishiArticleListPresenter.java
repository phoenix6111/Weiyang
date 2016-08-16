package com.wanghaisheng.weiyang.presenter.caipu;

import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseListLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.template_lib.presenter.common.CommonListView;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.presenter.common.RequestWrapperFactory;
import com.wanghaisheng.weiyang.presenter.common.RequestWrapperHandler;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/6/19.
 */
public class MeishiArticleListPresenter extends BaseListLoadablePresenter<CommonListView> {

    @Inject
    RequestWrapperFactory wrapperFactory;
    @Inject
    ICache iCache;
    RequestWrapperHandler wrapperHandler;
    List<ChannelEntity> channelEntities;
    private String moduleName;
    private boolean isLoadingMore = false;
    private int modulePage = 1;
    private int channelIndex = 0;

    @Inject
    public MeishiArticleListPresenter(){}

    public void loadListData(final int page) {
//        LogUtils.d("  page  "+page);
        Observable observable = null;
        if(page == 0) {
//            showViewLoading();
            if(wrapperHandler == null) {
                wrapperHandler = new RequestWrapperHandler(channelEntities,wrapperFactory);
            }
            observable = wrapperHandler.refreshBeanList();
        } else if(page == 1){
//            showViewLoading();
            if(wrapperHandler == null) {
                wrapperHandler = new RequestWrapperHandler(channelEntities,wrapperFactory);
            }
//            wrapperHandler.reset();
            //设置上一次查看的位置
            wrapperHandler.setModulePage(modulePage);
            wrapperHandler.setChannelIndex(channelIndex);
            observable = wrapperHandler.getMultiBeanList();
        } else {
            if(isLoadingMore) {
                return;
            }
            observable = wrapperHandler.getMultiBeanList();
            isLoadingMore = true;
        }

        modulePage = wrapperHandler.getModulePage();
        channelIndex = wrapperHandler.getChannelIndex();

        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<List<BaseBean>>() {
                    @Override
                    protected void onError(AppException ex) {
                        CommonListView iView = getView();
                        if(iView != null) {
                            if(page == 0) {
                                hideViewLoading();
                                iView.loadError(IView.LOAD_TYPE_REFRESH,ex);
                            } else if(page == 1) {
                                hideViewLoading();
                                iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                            } else {
                                iView.loadError(IView.LOAD_TYPE_LOADMORE,ex);
                            }
                        }
                    }

                    @Override
                    public void onNext(List<BaseBean> baseBeen) {
                        if(page == 0||page == 1) {
                            hideViewLoading();
//                            LogUtils.d("hide view loading....");
                        }

                        CommonListView iView = getView();
                        if(iView != null) {
                            iView.renderData(baseBeen);
                            isLoadingMore = false;
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }

    /**
     * 设置模块数据
     * @param channelEntity
     */
    public void setChannelEntity(ZhuantiChannelEntity channelEntity) {
        this.channelEntities = channelEntity.getChannelEntities();
//        this.moduleName = channelEntity.getChannel_tag();
        wrapperHandler = new RequestWrapperHandler(channelEntities,wrapperFactory);
//        LogUtils.d("setchannel entities  wrapperhandler  set null");

        Serializable tempModulePage = iCache.getSerializable("saved_module_page_"+moduleName);
        if(tempModulePage != null) {
            modulePage = (int)tempModulePage;
        }
        Serializable tempChannelIndex = iCache.getSerializable("saved_channel_index_"+moduleName);
        if(tempChannelIndex != null) {
            channelIndex = (int) tempChannelIndex;
        }
    }

    @Override
    public void pause() {
        super.pause();
        /*iCache.saveObject("saved_module_page_"+moduleName,modulePage, CacheHelper.MODULE_CACHE_TIME);
        iCache.saveObject("saved_channel_index_"+moduleName,channelIndex,CacheHelper.MODULE_CACHE_TIME);*/
    }
}
