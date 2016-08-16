package com.wanghaisheng.template_lib.appexception;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.BaseApplication;

/**
 * Created by sheng on 2016/6/12.
 */
public class AppException extends Exception {

    public static final int ERROR_TYPE_NETWORK = 1;
    public static final int ERROR_TYPE_NODATA = 2;
    public static final int ERROR_TYPE_PERMISSION = 4;
    public static final int ERROR_TYPE_PARSE = 5;
    public static final int ERROR_TYPE_UNKNOWN = 6;
    public static final int ERROR_TYPE_NOT_FOUND = 7;
    public static final int ERROR_TYPE_UNIQUE = 8;
    public static final int ERROR_TYPE_VALIDATE = 9;
    public static final int ERROR_TYPE_SERVER = 10;

    private final int code;
    private String displayMessage;

    public AppException(String displayMessage, int code) {
        super(displayMessage);
        LogUtils.d("AppException displaymessage code "+code+ " display message  "+displayMessage);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public AppException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.displayMessage = ErrorMessageFactory.getMessage(BaseApplication.context(),code);
        LogUtils.d("AppException throwable  code "+code+ " display message  "+displayMessage);
    }

    public int getCode() {
        return code;
    }
    public String getDisplayMessage() {
        return displayMessage;
    }
    public void setDisplayMessage(String msg) {
        this.displayMessage = msg;
    }


}
