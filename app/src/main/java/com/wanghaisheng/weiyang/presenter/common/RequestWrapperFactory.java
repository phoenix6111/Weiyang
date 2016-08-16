package com.wanghaisheng.weiyang.presenter.common;

import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.repository.DataHandlerFactory;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepository;
import com.wanghaisheng.weiyang.presenter.common.requestwrapper.CommonRequestWrapper;
import com.wanghaisheng.weiyang.presenter.common.requestwrapper.RequestWrapper;
import com.wanghaisheng.weiyang.presenter.common.requestwrapper.SohuRequestWrapper;

/**
 * Created by sheng on 2016/6/17.
 * RequestWrapper工厂类，单例对象
 * 根据ChannelEntity获取RequestWrapper对象
 */

public class RequestWrapperFactory {

    DataHandlerFactory dataHandlerFactory;
    SohuRepository sohuRepository;

    public RequestWrapperFactory(DataHandlerFactory dataHandlerFactory,SohuRepository sohuRepository){
        this.dataHandlerFactory = dataHandlerFactory;
        this.sohuRepository = sohuRepository;
    }

    /**
     * 获取首页的RequestWrapper
     * @param channelEntity
     * @return
     */
    public RequestWrapper getRequestWrapper(ChannelEntity channelEntity) {
        String moduleIdentity = channelEntity.getChannelIdentity();
        String[] moduleInfos = moduleIdentity.split("\\|");
        String module = moduleInfos[0];

        if(ModuleConstants.MODULE_IDENTITY_SOHU.equals(module)) {
            return new SohuRequestWrapper(channelEntity,sohuRepository);
        }

        return new CommonRequestWrapper(channelEntity,dataHandlerFactory);

    }

}
