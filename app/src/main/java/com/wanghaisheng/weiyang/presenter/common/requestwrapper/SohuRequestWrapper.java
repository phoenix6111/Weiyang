package com.wanghaisheng.weiyang.presenter.common.requestwrapper;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuData;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Author: sheng on 2016/8/9 22:38
 * Email: 1392100700@qq.com
 */
public class SohuRequestWrapper implements RequestWrapper {
    private int page;
    private int tempPage;
    private int clientPage = 1;//调用端传来的page
    private SohuRepository repository;
    private ChannelEntity channelEntity;
    private String category;
    private String tag;

    public SohuRequestWrapper(ChannelEntity channelEntity, SohuRepository repository) {
        this.repository = repository;
        this.channelEntity = channelEntity;

        String moduleIdentity = channelEntity.getChannelIdentity();
        String[] moduleInfos = moduleIdentity.split("\\|");
        this.category = moduleInfos[1];
        this.tag = moduleInfos[2];
    }

    @Override
    public Observable<List<BaseBean>> getBeanList() {
        LogUtils.d("clientPage ="+clientPage);
        if(clientPage <= 1) {
            return refreshList();
        } else {
            return repository.subscribeArticleBean(category,tag,page)
                    .map(new Func1<SohuData, List<BaseBean>>() {
                        @Override
                        public List<BaseBean> call(SohuData articleBeanResult) {

                            List<BaseBean> result = new ArrayList<BaseBean>();
                            List<BaseBean> tempList = articleBeanResult.getList();
                            for (BaseBean baseBean : tempList) {
                                baseBean.setModuleTitle(channelEntity.getModuleTitle());
                                baseBean.setZhuanti(channelEntity.getChannelTag());
                                baseBean.setCZhuanti(channelEntity.getChannelTitle());
                                baseBean.setCTag(channelEntity.getCtag());
                                result.add(baseBean);
                            }

                            //每一次请求都在上一次请求的page之下-1
                            tempPage = page-1;
                            LogUtils.d(tempPage);
                            return result;
                        }
                    });
        }

    }

    @Override
    public Observable<List<BaseBean>> refreshList() {
        return repository.networkArticleBeanResult(category,tag,0)
                .map(new Func1<SohuData, List<BaseBean>>() {
                    @Override
                    public List<BaseBean> call(SohuData sohuData) {
                        List<BaseBean> result = new ArrayList<BaseBean>();
                        List<BaseBean> tempList = sohuData.getList();
                        for (BaseBean baseBean : tempList) {
                            baseBean.setModuleTitle(channelEntity.getModuleTitle());
                            baseBean.setZhuanti(channelEntity.getChannelTag());
                            baseBean.setCZhuanti(channelEntity.getChannelTitle());
                            baseBean.setCTag(channelEntity.getCtag());
                            result.add(baseBean);
                        }

                        //第一次请求的时候获取最大page，然后接下来的每一次请求都在上一次请求的page之下-1
                        tempPage = sohuData.getPage();
                        return result;
                    }
                });
    }

    @Override
    public void nextIndex() {
        this.page = page-1;
    }

    @Override
    public void setPage(int source) {
        this.clientPage = source;
        page = tempPage;
        LogUtils.d("set page clientPage ="+clientPage+"  page "+page);
    }
}
