package com.wanghaisheng.template_lib.datasource.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sheng on 2016/5/28.
 */
public class BaseResponseResult {
    @SerializedName("status_no")
    private int statusCode;
    @SerializedName("status_msg")
    private String statusMessage;
    @SerializedName("time")
    private long time;

    public int getStatusCode()
    {
        return this.statusCode;
    }

    public String getStatusMessage()
    {
        return this.statusMessage;
    }

    public long getTime()
    {
        return this.time;
    }

    public void setStatusCode(int paramInt)
    {
        this.statusCode = paramInt;
    }

    public void setStatusMessage(String paramString)
    {
        this.statusMessage = paramString;
    }

    public void setTime(long paramLong)
    {
        this.time = paramLong;
    }
}
