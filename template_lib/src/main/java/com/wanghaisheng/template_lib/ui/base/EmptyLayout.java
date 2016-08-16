package com.wanghaisheng.template_lib.ui.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.utils.TDevice;


public class EmptyLayout extends LinearLayout implements View.OnClickListener {//

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int RETRY = 5;

    protected TextView tvLoading;
    protected LinearLayout progressContainer;
    protected RelativeLayout rlProgress;
    protected RelativeLayout rlError;
    protected TextView tvError;
    protected ImageView errorImage;
    private String strNoDataContent = "暂无内容";
    private boolean clickEnable = true;
    private View contentView;

    private String errorMsg;

    private OnClickListener listener;

    private int mErrorState;

    private Context context;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        contentView = View.inflate(context, R.layout.common_empty_view, null);
        tvLoading = (TextView) contentView.findViewById(R.id.tvLoading);
        progressContainer = (LinearLayout) contentView.findViewById(R.id.progress_container);
        rlProgress = (RelativeLayout) contentView.findViewById(R.id.rlProgress);
        rlError = (RelativeLayout) contentView.findViewById(R.id.rlError);
        tvError = (TextView) contentView.findViewById(R.id.tvError);
        errorImage = (ImageView) contentView.findViewById(R.id.iv_error_img);

        errorImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });

        setOnClickListener(this);
        addView(contentView);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    /**
     *
     * @param msg
     */
    public void setErrorMessage(String msg) {
        this.errorMsg = msg;
        tvError.setText(msg);
    }

    /**
     * 新添设置背景
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    public void setErrorImag(int imgResource) {
        try {
            errorImage.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    /**
     * 设置状态
     * @param i
     */
    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case NETWORK_ERROR:
            mErrorState = NETWORK_ERROR;
            LogUtils.d("empty layout network loadError   ");
            if (TDevice.hasInternet()) {
                tvError.setText(R.string.error_view_load_error);
                errorImage.setBackgroundResource(R.drawable.page_icon_faild);
            } else {
                tvError.setText(R.string.error_msg_network);
                errorImage.setBackgroundResource(R.drawable.page_icon_network);
            }
            rlError.setVisibility(VISIBLE);
            rlProgress.setVisibility(GONE);
            clickEnable = true;
            break;
        case NETWORK_LOADING:
            mErrorState = NETWORK_LOADING;
            rlProgress.setVisibility(VISIBLE);
            rlError.setVisibility(GONE);
            tvError.setText(R.string.error_view_loading);
            clickEnable = false;
            break;
        case NODATA:
            LogUtils.d("empty layout no data loadError   ");
            mErrorState = NODATA;
            errorImage.setBackgroundResource(R.drawable.page_icon_empty);
            rlProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            rlError.setVisibility(VISIBLE);
            break;
        case RETRY:
            LogUtils.d("empty layout no loadError clickable  ");
            mErrorState = RETRY;
            errorImage.setBackgroundResource(R.drawable.page_icon_empty);
            rlError.setVisibility(View.VISIBLE);
            rlProgress.setVisibility(View.GONE);
            clickEnable = true;
            break;
        case HIDE_LAYOUT:
            mErrorState = HIDE_LAYOUT;
            setVisibility(View.GONE);
            break;

        default:
            break;
        }
        mErrorState = i;
    }

    private void setTvNoDataContent() {
        if (!TextUtils.isEmpty(strNoDataContent))
            tvError.setText(strNoDataContent);
        else
            tvError.setText(R.string.error_msg_no_data);
    }

    private void setClickableNoDataContent() {
        tvError.setText(R.string.error_view_no_data_enableclick);
    }

    /**
     * 注入屏幕点击事件
     * @param listener
     */
    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置是否可见
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        } else {
            contentView.setVisibility(visibility);
        }
        super.setVisibility(visibility);
    }

    /***************************************
     * 设置状态，以方便调用
     ***************************************/

    /**
     * 网络错误
     */
    public void setNetworkError() {
        setErrorType(NETWORK_ERROR);
    }

    /**
     * 正在加载中
     */
    public void setNetworkLoading() {
        setErrorType(NETWORK_LOADING);
    }

    /**
     * 无数据，且不能点击
     */
    public void setNodata() {
        setErrorType(NODATA);
    }

    /**
     * 无数据，但可以点击
     */
    public void setRetry(String msg) {
        if(!TextUtils.isEmpty(msg)) {
            setErrorMessage(msg);
        } else {
            setClickableNoDataContent();
        }

        setErrorType(RETRY);
    }

    /**
     * 移除
     */
    public void dismiss() {
        setErrorType(HIDE_LAYOUT);
    }


}