package com.wanghaisheng.weiyang.datasource.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/6/21.
 * 专题的channelEntity
 */
public class ZhuantiChannelEntity implements Serializable {
    private static final long serialVersionUID = -4683346820807408053L;
    private String channel_tag;
    private String channel_title;
    @SerializedName("channel_entities")
    private List<ChannelEntity> channelEntities;

    public String getChannel_tag() {
        return channel_tag;
    }

    public void setChannel_tag(String channel_tag) {
        this.channel_tag = channel_tag;
    }

    public String getChannel_title() {
        return channel_title;
    }

    public void setChannel_title(String channel_title) {
        this.channel_title = channel_title;
    }

    public List<ChannelEntity> getChannelEntities() {
        return channelEntities;
    }

    public void setChannelEntities(List<ChannelEntity> channelEntities) {
        this.channelEntities = channelEntities;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZhuantiChannelEntity) {
            ZhuantiChannelEntity entity = (ZhuantiChannelEntity) obj;
            return this.getChannel_tag().equals(entity.getChannel_tag());
        }
        return super.equals(obj);
    }
}
