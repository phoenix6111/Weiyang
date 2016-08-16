package com.wanghaisheng.weiyang.datasource.repository.sohu;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.util.List;

/**
 * Author: sheng on 2016/8/9 18:22
 * Email: 1392100700@qq.com
 */
public class SohuData {

    //最大的页码
    private int page;
    private List<BaseBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<BaseBean> getList() {
        return list;
    }

    public void setList(List<BaseBean> list) {
        this.list = list;
    }
}
