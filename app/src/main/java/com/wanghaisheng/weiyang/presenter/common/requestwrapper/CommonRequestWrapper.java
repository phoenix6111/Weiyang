package com.wanghaisheng.weiyang.presenter.common.requestwrapper;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.repository.DataHandlerFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by sheng on 2016/6/17.
 */
public class CommonRequestWrapper implements RequestWrapper {
    private ChannelEntity channelEntity;
    private String moduleIdentity;
    private int page = 1;
    DataHandlerFactory dataHandlerFactory;

    public CommonRequestWrapper(ChannelEntity channelEntity, DataHandlerFactory dataHandlerFactory) {
        this.channelEntity = channelEntity;
        this.moduleIdentity = channelEntity.getChannelIdentity();
        this.dataHandlerFactory = dataHandlerFactory;
    }

    @Override
    public Observable<List<BaseBean>> getBeanList() {
//        LogUtils.d("module Identitty  "+moduleIdentity);
        return dataHandlerFactory.subscribeData(moduleIdentity,page)
                .map(new Func1<List<BaseBean>, List<BaseBean>>() {
                    @Override
                    public List<BaseBean> call(List<BaseBean> baseBeen) {
                        List<BaseBean> results = new ArrayList<BaseBean>();
                        for(BaseBean baseBean : baseBeen) {
                            baseBean.setModuleTitle(channelEntity.getModuleTitle());
                            baseBean.setZhuanti(channelEntity.getChannelTag());
                            baseBean.setCZhuanti(channelEntity.getChannelTitle());
                            baseBean.setCTag(channelEntity.getCtag());
                            results.add(baseBean);
                        }

                        return results;
                    }
                });
    }

    @Override
    public Observable<List<BaseBean>> refreshList() {
        setPage(1);
        return dataHandlerFactory.networkData(moduleIdentity,page)
                .map(new Func1<List<BaseBean>, List<BaseBean>>() {
                    @Override
                    public List<BaseBean> call(List<BaseBean> baseBeen) {
                        List<BaseBean> results = new ArrayList<BaseBean>();
                        for(BaseBean baseBean : baseBeen) {
                            baseBean.setModuleTitle(channelEntity.getModuleTitle());
                            baseBean.setZhuanti(channelEntity.getChannelTag());
                            baseBean.setCZhuanti(channelEntity.getChannelTitle());
                            baseBean.setCTag(channelEntity.getCtag());
                            results.add(baseBean);
                        }

                        return results;
                    }
                });
    }

    @Override
    public void nextIndex() {
        page += 1;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
