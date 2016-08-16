package com.wanghaisheng.weiyang.presenter.common;

import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;

import java.util.List;

/**
 * Created by sheng on 2016/6/5.
 */
public interface ChannelView extends IView {
    void renderSavedChannelBean(List<ZhuantiChannelEntity> channelEntities);
}
