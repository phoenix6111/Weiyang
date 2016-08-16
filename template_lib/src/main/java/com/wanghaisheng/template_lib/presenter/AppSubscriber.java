package com.wanghaisheng.template_lib.presenter;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.wanghaisheng.template_lib.appexception.AppException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by sheng on 2016/6/12.
 */
public abstract class AppSubscriber<T> extends Subscriber<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    protected AppSubscriber() {

    }


    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while(throwable.getCause() != null){
            e = throwable;
            throwable = throwable.getCause();
        }

        AppException ex;
        if(e instanceof AppException) {
            ex = (AppException) e;
            onError(ex);
            return;
        }

        if(e instanceof UnknownHostException || e instanceof ConnectException
                || e instanceof IOException) {
            ex = new AppException(e,AppException.ERROR_TYPE_NETWORK);
        } else if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;

            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                    ex = new AppException(e, AppException.ERROR_TYPE_PERMISSION);        //权限错误，需要实现
                    break;
                case NOT_FOUND:
                    ex = new AppException(e, AppException.ERROR_TYPE_NOT_FOUND);        //
                    break;
                case REQUEST_TIMEOUT:
                    ex = new AppException(e, AppException.ERROR_TYPE_NETWORK);  //均视为网络错误
                    break;
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                case GATEWAY_TIMEOUT:
                default:
                    ex = new AppException(e, AppException.ERROR_TYPE_SERVER);  //服务器错误
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new AppException(e, AppException.ERROR_TYPE_PARSE);

        } else {
            ex = new AppException(e, AppException.ERROR_TYPE_UNKNOWN);

        }

        onError(ex);
    }


    /**
     * 错误回调
     */
    protected abstract void onError(AppException ex);


    @Override
    public void onCompleted() {

    }


}
