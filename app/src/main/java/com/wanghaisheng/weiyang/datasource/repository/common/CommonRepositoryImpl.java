package com.wanghaisheng.weiyang.datasource.repository.common;

import android.content.Context;
import android.content.res.AssetManager;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanghaisheng.template_lib.datasource.cache.ICache;
import com.wanghaisheng.weiyang.database.Channel;
import com.wanghaisheng.weiyang.database.ChannelDao;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelBean;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sheng on 2016/6/4.
 */
public class CommonRepositoryImpl implements CommonRepository {
    private static final String TAG = "CommonRepositoryImpl";

    public static final String CHANNEL_JSON_NAME = "channel_data.json";

    private ICache iCache;
    private Context context;
    private ChannelDao channelDao;

    public CommonRepositoryImpl(ICache iCache, Context context, ChannelDao channelDao) {
        this.iCache = iCache;
        this.context = context;
        this.channelDao = channelDao;
    }

    /**
     * 清除程序缓存数据
     */
    public void clearCacheData() {
        iCache.clearCacheData();
    }


    /**
     * 初始化首页index所有模块的信息，模块定制时从中获取信息
     * @return
     */
    @Override
    public void initChannelEntities() {
        Gson gson = new Gson();
        List<ZhuantiChannelBean> results = gson.fromJson(getJsonString(CHANNEL_JSON_NAME)
                ,new TypeToken<List<ZhuantiChannelBean>>(){}.getType());

        for(ZhuantiChannelBean channelBean : results) {
            Channel channel = new Channel();
            channel.setChannel_tag(channelBean.getModule_tag());
            channel.setChannel_title(channelBean.getModule_title());
            channel.setChannel_json(gson.toJson(channelBean.getChannel_tags()));
            channelDao.insert(channel);
        }

    }

    public List<ZhuantiChannelEntity> getChannelEntitiesByModule(final String module) {
        Channel channel = channelDao.queryBuilder().where(ChannelDao.Properties.Channel_tag.eq(module)).unique();
        if(channel != null) {
            String channelJson = channel.getChannel_json();
            Gson gson = new Gson();
            return gson.fromJson(channelJson,new TypeToken<List<ZhuantiChannelEntity>>(){}.getType());
        }

        return new ArrayList<>();
    }

    public Serializable getCachedData(String key) {
        return iCache.getSerializable(key);
    }

    private String getJsonString(String jsonName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(jsonName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LogUtils.d(e);
        }

        return stringBuilder.toString();
    }
}
