package com.wanghaisheng.weiyang.datasource.repository;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.appexception.ErrorMessageFactory;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.datasource.beans.UserResponseResult;

import rx.Observable;
import rx.functions.Func1;


/**
 * 不打破RxJava的链式调用，参考：http://mrfu.me/2016/01/10/RxWeekend/#tips7
 */
public class SchedulersCompat {

    private static final Observable.Transformer userApiTransformer = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            final Context context = AppContext.context();
            return ((Observable) observable).flatMap(new Func1<UserResponseResult, Observable<UserResponseResult>>() {
                @Override
                public Observable<UserResponseResult> call(UserResponseResult responseResult) {
                    LogUtils.d(responseResult);
                    LogUtils.d("statues code  "+responseResult.getStatusCode());
                    if(200 == responseResult.getStatusCode()) {
                        return Observable.just(responseResult);
                    } else if(804 == responseResult.getStatusCode()) {
                        return Observable.error(new AppException(ErrorMessageFactory.getMessage(context,AppException.ERROR_TYPE_NOT_FOUND),AppException.ERROR_TYPE_NOT_FOUND));
                    } else if(801 == responseResult.getStatusCode()) {
                        return Observable.error(new AppException(ErrorMessageFactory.getMessage(context,AppException.ERROR_TYPE_UNIQUE),AppException.ERROR_TYPE_UNIQUE));
                    }else if(802 == responseResult.getStatusCode()) {
                        return Observable.error(new AppException(ErrorMessageFactory.getMessage(context,AppException.ERROR_TYPE_VALIDATE),AppException.ERROR_TYPE_VALIDATE));
                    } else if(803 == responseResult.getStatusCode()) {
                        return Observable.error(new AppException(ErrorMessageFactory.getMessage(context,AppException.ERROR_TYPE_SERVER),AppException.ERROR_TYPE_SERVER));
                        //800为成功
                    }

                    return Observable.just(responseResult);
                }
            });
        }
    };

    public static <UserResponseResult> Observable.Transformer<UserResponseResult, UserResponseResult> applyUserResponseSchedulers() {
        return (Observable.Transformer<UserResponseResult, UserResponseResult>) userApiTransformer;
    }


}