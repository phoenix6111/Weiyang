package com.wanghaisheng.weiyang.datasource.beans;

import java.util.List;

/**
 * Created by sheng on 2016/6/10.
 */
public class ScienceArticleResult {

    private boolean ok;

    private List<ScienceArticle> result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<ScienceArticle> getResult() {
        return result;
    }

    public void setResult(List<ScienceArticle> result) {
        this.result = result;
    }


}
