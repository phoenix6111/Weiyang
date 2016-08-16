package com.wanghaisheng.template_lib.appexception;

import android.content.Context;

import com.wanghaisheng.template_lib.R;

public class ErrorMessageFactory {
    private static final String TAG = "ErrorMessageFactory";

    public static String getMessage(Context context, int errorCode) {
        switch (errorCode) {
            case AppException.ERROR_TYPE_NETWORK:
                return context.getString(R.string.error_msg_network);
            case AppException.ERROR_TYPE_PERMISSION:
                return context.getString(R.string.error_msg_permission);
            case AppException.ERROR_TYPE_PARSE:
                return context.getString(R.string.error_msg_parse);
            case AppException.ERROR_TYPE_NODATA:
                return context.getString(R.string.error_msg_no_data);
            case AppException.ERROR_TYPE_NOT_FOUND:
                return context.getString(R.string.error_msg_notfound);
            case AppException.ERROR_TYPE_UNIQUE:
                return context.getString(R.string.error_msg_unique);
            case AppException.ERROR_TYPE_VALIDATE:
                return context.getString(R.string.error_msg_validate);
            case AppException.ERROR_TYPE_SERVER:
                return context.getString(R.string.error_msg_server);
            case AppException.ERROR_TYPE_UNKNOWN:
                return context.getString(R.string.error_view_unknown_error);

        }

        return context.getString(R.string.error_view_unknown_error);
    }
}
