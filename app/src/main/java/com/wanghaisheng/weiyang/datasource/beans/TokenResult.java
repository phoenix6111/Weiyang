package com.wanghaisheng.weiyang.datasource.beans;

import com.wanghaisheng.template_lib.datasource.beans.BaseResponseResult;

/**
 * Created by sheng on 2016/5/21.
 */
public class TokenResult extends BaseResponseResult {
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
