package com.wanghaisheng.template_lib.datasource.beans;

import java.io.Serializable;

/**
 * Author: sheng on 2016/8/7 16:59
 * Email: 1392100700@qq.com
 */
public class BaseBean implements Serializable{

    protected String moduleName;//  标识该bean是属于哪一个module的：如微信精选，生活百科。。。
    protected String moduleTitle;// module的中文
    protected String tag;//标识该bean是属于专题的哪一个tag的
    protected String cTag;//标识中文名
    protected String zhuanti;//标识该bean属于哪一个专题，在列表的时候点击打开
    protected String cZhuanti;//专题中文名：列表时显示
    protected boolean liked;
    protected Boolean isCollected;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getZhuanti() {
        return zhuanti;
    }

    public void setZhuanti(String zhuanti) {
        this.zhuanti = zhuanti;
    }

    public String getCTag() {
        return cTag;
    }

    public void setCTag(String cTag) {
        this.cTag = cTag;
    }

    public String getCZhuanti() {
        return cZhuanti;
    }

    public void setCZhuanti(String cZhuanti) {
        this.cZhuanti = cZhuanti;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Boolean isCollected) {
        this.isCollected = isCollected;
    }

}
