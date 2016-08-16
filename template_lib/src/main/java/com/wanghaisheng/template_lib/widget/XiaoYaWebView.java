package com.wanghaisheng.template_lib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apkfuns.logutils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sheng on 2016/4/15.
 */
public class XiaoYaWebView extends WebView {

    private String basicUA;
    private Map<String, String> header;

//    @Inject
//    RequestHelper mRequestHelper;

    public XiaoYaWebView(Context context) {
        super(context);
        init();
    }

    public XiaoYaWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCallBack(XiaoYaWebViewCallBack callBack) {
        this.callBack = callBack;
    }

    public void removeCallBack() {
        this.callBack = null;
    }

    public class XiaoYaChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            LogUtils.d("onConsoleMessage:" + consoleMessage.message() + ":" + consoleMessage.lineNumber());
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private void init() {
        WebSettings settings = getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        //设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(false);
        settings.setUseWideViewPort(true);
        /**
         * 将图片下载阻塞，然后在浏览器的OnPageFinished事件中设置 webView.getSettings().setBlockNetworkImage(false);
         * 通过图片的延迟载入，让网页能更快地显示。
         */
//        settings.setBlockNetworkImage(true);
        if (Build.VERSION.SDK_INT > 6) {
            settings.setAppCacheEnabled(true);
            settings.setLoadWithOverviewMode(true);
        }
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        String path = getContext().getFilesDir().getPath();
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(path);
        /**
         * 默认情况html代码下载到WebView后，webkit开始解析网页各个节点，发现有外部样式文件或者外部脚本文件时，
         * 会异步发起网络请求下载文件，但如果在这之前也有解析到image节点，那势必也会发起网络请求下载相应的图片。在网络情况较差的情况下，
         * 过多的网络请求就会造成带宽紧张，影响到css或js文件加载完成的时间，造成页面空白loading过久。
         * 解决的方法就是告诉WebView先不要自动加载图片，等页面finish后再发起图片加载。
         */
        if(Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        initWebViewClient();
        setWebChromeClient(new XiaoYaChromeClient());

    }

    private void initWebViewClient() {
        CookieManager.getInstance().setAcceptCookie(true);
        setWebViewClient(new XiaoYaWebClient());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private class XiaoYaWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            logger.debug(Uri.decode(url));

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
//            view.getSettings().setBlockNetworkImage(false);//当页面加载完成时还原图片下载
            if (callBack != null) {
                callBack.onFinish();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (callBack != null) {
//                WebViewError webViewError = new WebViewError(error.getErrorCode(),error.getDescription());
                callBack.onError(error);
            }
        }
    }



    private void setUA(int i) {
        if (this.basicUA != null) {
//            getSettings().setUserAgentString(this.basicUA + " isp/" + i + " network/" + i);
        }
    }

    public void loadUrl(String url) {
        setUA(-1);
        if (header == null) {
            header = new HashMap<>();
            header.put("Accept-Encoding", "gzip, deflate, sdch");
        }
        super.loadUrl(url, header);
    }

    private XiaoYaWebViewCallBack callBack;


    public interface XiaoYaWebViewCallBack {

        void onFinish();

        void onUpdatePager(int page, int total);

        void onError(WebResourceError error);

    }

    public class WebViewError{
        int errorCode;
        CharSequence errorMsg;

        public WebViewError(){}

        public WebViewError(int errorCode,CharSequence errorMsg){
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public CharSequence getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(CharSequence errorMsg) {
            this.errorMsg = errorMsg;
        }
    }


    private OnScrollChangedCallback mOnScrollChangedCallback;

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        LogUtils.v("onscroll changed...................................");
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimers();
    }

    @Override
    public void destroy() {
        //releaseAllWebViewCallback();
        //releaseWebView();
        super.destroy();
    }

    private void releaseWebView() {
        getSettings().setBuiltInZoomControls(true);
        setVisibility(GONE);
        removeAllViews();
    }



}
