package com.wanghaisheng.weiyang.datasource.beans;

/**
 * Created by sheng on 2016/6/5.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * channel实体类，包含channel英文名和中文名
 */
public class ChannelEntity implements Serializable {
    private static final long serialVersionUID = 1540911554328023801L;

    @SerializedName("channel_identity")
    private String channelIdentity;//channel标识：形式为： module|category|tag
    @SerializedName("channel_title")
    private String channelTitle;
    @SerializedName("channel_tag")
    private String channelTag;
    private String ctag;
    @SerializedName("module_title")
    private String moduleTitle;


    public String getChannelIdentity() {
        return channelIdentity;
    }

    public void setChannelIdentity(String channelIdentity) {
        this.channelIdentity = channelIdentity;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getCtag() {
        return ctag;
    }

    public void setCtag(String ctag) {
        this.ctag = ctag;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChannelEntity) {
            ChannelEntity entity = (ChannelEntity) obj;
            return this.getChannelIdentity().equals(entity.getChannelIdentity());
        }
        return super.equals(obj);
    }
}
