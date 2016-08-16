package com.wanghaisheng.weiyang.presenter.common.requestwrapper;

import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.presenter.common.RequestWrapperFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sheng on 2016/7/30.
 * RequestWrapper 池 ，重复利用
 */
public class RequestWrapperPool {
    private Map<ChannelEntity,RequestWrapper> requestWrapperList;
    private RequestWrapperFactory wrapperFactory;

    public RequestWrapperPool(RequestWrapperFactory wrapperFactory) {
        this.wrapperFactory = wrapperFactory;
        requestWrapperList = new HashMap<>();
    }

    public RequestWrapper getRequestWrapper(ChannelEntity channelEntity) {
        RequestWrapper requestWrapper = requestWrapperList.get(channelEntity);
        if(requestWrapper == null) {
            requestWrapper = wrapperFactory.getRequestWrapper(channelEntity);
            requestWrapperList.put(channelEntity,requestWrapper);
        }

        return requestWrapper;
    }


}
