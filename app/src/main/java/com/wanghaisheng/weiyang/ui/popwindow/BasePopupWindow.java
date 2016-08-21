package com.wanghaisheng.weiyang.ui.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author: sheng on 2016/8/17 16:58
 * Email: 1392100700@qq.com
 */
public abstract class BasePopupWindow extends PopupWindow {

    //PoupupWindow的宽和高
    protected int mWidth;
    protected int mHeight;
    protected View mConvertView;

    protected Context mContext;

    public BasePopupWindow(Context context) {
        this.mContext = context;
        //设置宽和高
        calculateWidthAndHeight();
        mConvertView = LayoutInflater.from(mContext).inflate(getLayoutId(),null);
        setContentView(mConvertView);
        //设置宽度和高度
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 布局文件的resId
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * dialog的高度占window高度的分比
     * @return
     */
    protected abstract float heightRatio();

    //设置宽和高，宽为屏幕的宽度，高为屏幕高度的0.7倍
    private void calculateWidthAndHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels*heightRatio());
    }

}
