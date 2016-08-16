package com.wanghaisheng.weiyang.presenter.common;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.presenter.common.requestwrapper.RequestWrapper;
import com.wanghaisheng.weiyang.presenter.common.requestwrapper.RequestWrapperPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func2;

/**
 * Created by sheng on 2016/6/22.
 * 请求处理类
 * 根据module综合加载数据
 * 思路就是：获取所有首页的api：一次用两个api的数据，然后换另一批的两个，如此用完所有的首页的api，则modulePage+1
 * 进行第二次这个循环
 */
public class RequestWrapperHandler {
    private List<ChannelEntity> channelEntities = new ArrayList<>();
    private RequestWrapperPool requestWrapperPool;
    private int modulePage = 1;//模块的page
    private int channelIndex = 0;//在channelEntities中的游标


    public RequestWrapperHandler(List<ChannelEntity> channelEntities, RequestWrapperFactory wrapperFactory) {
        this.channelEntities = channelEntities;
        this.requestWrapperPool = new RequestWrapperPool(wrapperFactory);
    }

    /**
     * 设置模块page ，用以保存上一次最后查看的位置
     * @param modulePage
     */
    public void setModulePage(int modulePage) {
        this.modulePage = modulePage;
        this.channelIndex = 0;
    }

    public int getModulePage() {
        return modulePage;
    }

    public int getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
    }

    /**
     * 重置状态
     */
    public void reset() {
        modulePage = 1;
        channelIndex = 0;
    }

    /**
     * 动态获取
     * @return
     */
    public Observable<List<BaseBean>> getMultiBeanList() {
        /**
         * 如果已经到了一个循环的末尾，则modulepage+1，全部进行下一页
         */
        /**
         * 如果channelEntities的大小是奇数，则最后一个单独作请求，然后modulePage+1，
         * 如果channelEntities的size大小是偶数，则直接modulePage+1
         * 只有最后一个要做特殊处理
         */
        //如果是奇数，且是最后一个才做特殊处理
        if(channelIndex+1 == channelEntities.size()) {
            RequestWrapper requestWrapper = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex));
            requestWrapper.setPage(modulePage);
            return requestWrapper.getBeanList()
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            modulePage += 1;
                            channelIndex = 0;
                        }
                    });
        }

        //如果是最后一轮，则置0
        if(channelIndex>channelEntities.size()-1) {
            channelIndex = 0;
            modulePage += 1;
        }

        RequestWrapper requestWrapper1 = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex));
        requestWrapper1.setPage(modulePage);
        RequestWrapper requestWrapper2 = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex+1));
        requestWrapper2.setPage(modulePage);

//        return  Observable.mergeDelayError(requestWrapper1.getBeanList(),requestWrapper2.getBeanList());
        return Observable.zip(requestWrapper1.getBeanList(), requestWrapper2.getBeanList(), new Func2<List<BaseBean>, List<BaseBean>, List<BaseBean>>() {
            @Override
            public List<BaseBean> call(List<BaseBean> baseBeen, List<BaseBean> baseBeen2) {
                List<BaseBean> result = new ArrayList<>();
                result.addAll(baseBeen);
                result.addAll(baseBeen2);
                Collections.shuffle(result);

                return result;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                //正常情况下 channelIndex+2
                channelIndex += 2;
            }
        });
    }

    public Observable<List<BaseBean>> refreshBeanList() {
        modulePage = 1;
        channelIndex = 0;
        /**
         * 如果已经到了一个循环的末尾，则modulepage+1，全部进行下一页
         */
        /**
         * 如果channelEntities的大小是奇数，则最后一个单独作请求，然后modulePage+1，
         * 如果channelEntities的size大小是偶数，则直接modulePage+1
         * 只有最后一个要做特殊处理
         */
        //如果是奇数，且是最后一个才做特殊处理
        if(channelEntities.size()==1) {
            RequestWrapper requestWrapper = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex));
            requestWrapper.setPage(modulePage);
            return requestWrapper.refreshList()
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            modulePage += 1;
                            channelIndex = 0;
                        }
                    });
        }

        //如果是最后一轮，则置0
        if(channelIndex>channelEntities.size()-1) {
            channelIndex = 0;
            modulePage += 1;
        }

        RequestWrapper requestWrapper1 = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex));
        RequestWrapper requestWrapper2 = requestWrapperPool.getRequestWrapper(channelEntities.get(channelIndex+1));

//        return  Observable.mergeDelayError(requestWrapper1.getBeanList(),requestWrapper2.getBeanList());
        return Observable.zip(requestWrapper1.refreshList(), requestWrapper2.refreshList(), new Func2<List<BaseBean>, List<BaseBean>, List<BaseBean>>() {
            @Override
            public List<BaseBean> call(List<BaseBean> baseBeen, List<BaseBean> baseBeen2) {
                List<BaseBean> result = new ArrayList<>();
                result.addAll(baseBeen);
                result.addAll(baseBeen2);
                Collections.shuffle(result);

                return result;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                //正常情况下 channelIndex+2
                channelIndex += 2;
            }
        });
    }

    public void destroy() {
        this.channelEntities = null;
        this.requestWrapperPool = null;
        this.modulePage = 1;
        this.channelIndex = 0;
    }
}
