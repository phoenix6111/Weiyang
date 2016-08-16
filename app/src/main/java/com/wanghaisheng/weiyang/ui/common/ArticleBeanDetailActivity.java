package com.wanghaisheng.weiyang.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.appexception.ErrorMessageFactory;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.ui.base.EmptyLayout;
import com.wanghaisheng.template_lib.utils.AnimationUtils;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.template_lib.utils.WindowUtils;
import com.wanghaisheng.template_lib.widget.BrowserBottomPopupwindow;
import com.wanghaisheng.template_lib.widget.XiaoYaWebView;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.presenter.article.ArticleBeanDetailPresenter;
import com.wanghaisheng.weiyang.presenter.article.ArticleBeanDetailView;
import com.wanghaisheng.weiyang.presenter.common.CollectionAndLikePresenter;
import com.wanghaisheng.weiyang.presenter.common.CollectionView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.Bind;


/**
 * Created by sheng on 2016/6/19.
 */
public class ArticleBeanDetailActivity extends BaseActivity implements ArticleBeanDetailView
        ,BrowserBottomPopupwindow.BrowserPopupwindowListenter,CollectionView {
    private static final String TAG = "ArticleBeanDetailActivity";
    public static final String ARG_ARTICLEBEAN = "arg_articlebean";

    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pb_toolbar)
    ProgressBar toolbarProgress;
    protected XiaoYaWebView webView;
    @Bind(R.id.webview_container)
    protected FrameLayout webviewContainer;
    EmptyLayout emptyLayout;

    protected boolean isCollected;

    protected MenuItem mMenuItem;

    private BrowserBottomPopupwindow mPopupWindow;

    @Inject
    ArticleBeanDetailPresenter presenter;
    @Inject
    CollectionAndLikePresenter collectionPresenter;

    private BaseBean content;

    public void onReloadClick() {
        presenter.loadContentHtml(content);
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.content = (BaseBean) getIntent().getSerializableExtra(ARG_ARTICLEBEAN);
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((AppContext)getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_act_detail;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        webView = new XiaoYaWebView(ArticleBeanDetailActivity.this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webviewContainer.removeAllViews();
        webviewContainer.addView(webView);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.addJavascriptInterface(new JsInterface(ArticleBeanDetailActivity.this),"AndroidInterface");

        initPopupWindow();

    }

    private class JsInterface {

        private Context mContext;

        public JsInterface(Context mContext) {
            this.mContext = mContext;
        }

        /**
         * 在javascript中调用此方法加载剩余内容
         */
        @JavascriptInterface
        public void sohuLoadMoreData() {
//            ToastUtil.showCenterToast(mContext,"sohu 加载更多。。。");
            MeishiBean meishiBean = (MeishiBean) content;
            presenter.loadSohuMoreContent(meishiBean.getArticleId());
        }
    }

    @Override
    public void sohuLoadMoreContent(String str) {
//        LogUtils.d(str);
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(str);
        String newString = "";
        if (m.find()) {
            newString = m.replaceAll("<br>");
        }
        webView.loadUrl("javascript:appendData('"+newString+"')");
    }

    /**
     * 初始化popupwindow
     */
    private void initPopupWindow() {
        mPopupWindow = new BrowserBottomPopupwindow(ArticleBeanDetailActivity.this);
        mPopupWindow.setBrowserPopupwindowListener(this);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mPopupWindow.setLightValue(WindowUtils.getScreenBrightness(ArticleBeanDetailActivity.this));

    }

    private void initFontEvent() {
        mPopupWindow.setAnimationStyle(R.style.anim_dir_popwindow);
        mPopupWindow.showAtLocation(webviewContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        lightOff();
    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }


    @Override
    public void initData() {

        if (null != presenter) {
            presenter.attachView(this);
            collectionPresenter.attachView(this);

            presenter.loadContentHtml(content);
        }

    }

    @Override
    public void showLoading() {
        toolbarProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        toolbarProgress.startAnimation(AnimationUtils.getHiddenAlphaAnimation());
        toolbarProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articlebean_detail, menu);
        mMenuItem = menu.findItem(R.id.menu_collect);

        checkCollected();

        updateCollectionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void checkCollected() {
        collectionPresenter.checkIfCollected(content);
    }

    public void updateCollectionsMenu() {
        if (isCollected) {
            mMenuItem.setIcon(R.drawable.tabbar_collect_icon_press);
        } else {
            mMenuItem.setIcon(R.drawable.tabbar_collect_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_font) {
            initFontEvent();
        } else if (item.getItemId() == R.id.menu_collect) {
            collectOrUnCollect();
        }

        return true;
    }

    private void collectOrUnCollect() {
        if (isCollected) {
            content.setIsCollected(false);
        } else {
            content.setIsCollected(true);
        }
        collectionPresenter.collectOrUnCollect(content);
    }

    @Override
    protected void onDestroy() {
        if (null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }

        if (webView != null) {
            webviewContainer.removeView(webView);

            webView.removeCallBack();
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
       /* MobclickAgent.onPageEnd(TAG+content.getModuleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        /*MobclickAgent.onPageStart(TAG+content.getModuleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);*/          //统计时长
    }


    @Override
    public void lightOnclick(int progress) {
        WindowUtils.setScreenBrightness(ArticleBeanDetailActivity.this, progress);
    }

    @Override
    public void fontSelect(int font) {
        WebSettings settings = webView.getSettings();
        switch (font) {
            case BrowserBottomPopupwindow.FONT_SIZE_S:
                settings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case BrowserBottomPopupwindow.FONT_SIZE_M:
                settings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case BrowserBottomPopupwindow.FONT_SIZE_L:
                settings.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case BrowserBottomPopupwindow.FONT_SIZE_XL:
                settings.setTextSize(WebSettings.TextSize.LARGEST);
                break;
        }
    }

    @Override
    public void renderWebview(String webPageStr) {
//        LogUtils.d(webPageStr);
        webviewContainer.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(webPageStr)&& webPageStr.startsWith("http")) {
            webView.loadUrl(webPageStr);
            return;
        }
        webView.loadDataWithBaseURL("file:///android_asset/",webPageStr,"text/html", "utf-8", null);
    }

    protected void initEmptyLayout() {
        if(emptyLayout == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub);
            emptyLayout = (EmptyLayout) viewStub.inflate();
            emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReloadClick();
                }
            });
        }
    }

    @Override
    public void loadError(int loadType, AppException ex) {
        LogUtils.d(ex);
        initEmptyLayout();
        if(IView.LOAD_TYPE_FIRSTLOAD == loadType) {
//            toastUtil.showCenterToast(e.getDisplayMessage());
            if(ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                emptyLayout.setNetworkError();
            } else {
                emptyLayout.setRetry(ErrorMessageFactory.getMessage(getApplicationContext(),ex.getCode()));
            }
            webviewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateCollectionResult(boolean collected) {
        this.isCollected = collected;
        if(isCollected) {
            ToastUtil.showSnackbar("收藏成功",webView);
        } else {
            ToastUtil.showSnackbar("取消收藏",webView);
        }
        updateCollectionsMenu();
    }

    @Override
    public void updateCheckResult(boolean collected) {
        this.isCollected = collected;
        updateCollectionsMenu();
    }
}
