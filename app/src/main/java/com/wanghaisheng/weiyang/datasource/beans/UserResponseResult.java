package com.wanghaisheng.weiyang.datasource.beans;

import com.wanghaisheng.template_lib.datasource.beans.BaseResponseResult;

/**
 * Created by sheng on 2016/5/28.
 */
public class UserResponseResult extends BaseResponseResult {

    private User result;

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }
}
