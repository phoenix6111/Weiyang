package com.wanghaisheng.weiyang.datasource.repository.common;

import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/6/4.
 */
public interface CommonRepository {

    void initChannelEntities();

    List<ZhuantiChannelEntity> getChannelEntitiesByModule(final String module);

    Serializable getCachedData(String key);

    void clearCacheData();

}
