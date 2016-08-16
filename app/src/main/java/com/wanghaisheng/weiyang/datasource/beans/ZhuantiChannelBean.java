package com.wanghaisheng.weiyang.datasource.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/6/21.
 */
public class ZhuantiChannelBean implements Serializable {
    private static final long serialVersionUID = 6474545852403071927L;
    private String module_tag;//专题的模块
    private String module_title;
    private List<ZhuantiChannelEntity> channel_tags;//单个专题下的所有tags

    public String getModule_tag() {
        return module_tag;
    }

    public void setModule_tag(String module_tag) {
        this.module_tag = module_tag;
    }

    public List<ZhuantiChannelEntity> getChannel_tags() {
        return channel_tags;
    }

    public void setChannel_tags(List<ZhuantiChannelEntity> channel_tags) {
        this.channel_tags = channel_tags;
    }

    public String getModule_title() {
        return module_title;
    }

    public void setModule_title(String module_title) {
        this.module_title = module_title;
    }
}
