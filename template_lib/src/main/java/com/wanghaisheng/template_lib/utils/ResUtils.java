package com.wanghaisheng.template_lib.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by sheng on 2016/5/19.
 */
public class ResUtils {
    public static int[] getResArrays(Context context, int arrayResId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(arrayResId);

        int len = typedArray.length();
        int[] resIds = new int[len];
        for(int i=0; i<len; i++) {
            resIds[i] = typedArray.getResourceId(i,0);
        }

        typedArray.recycle();

        return resIds;
    }
}
