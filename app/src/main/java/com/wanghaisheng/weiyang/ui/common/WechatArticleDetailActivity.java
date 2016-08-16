package com.wanghaisheng.weiyang.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.template_lib.utils.WindowUtils;
import com.wanghaisheng.template_lib.widget.BrowserBottomPopupwindow;
import com.wanghaisheng.template_lib.widget.XiaoYaWebView;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.presenter.common.CollectionAndLikePresenter;
import com.wanghaisheng.weiyang.presenter.common.CollectionView;

import javax.inject.Inject;

import butterknife.Bind;


/**
 * Created by sheng on 2016/5/16.
 */
public class WechatArticleDetailActivity extends BaseActivity implements BrowserBottomPopupwindow.BrowserPopupwindowListenter
                ,CollectionView {
    private static final String TAG = "WechatArticleDetailActivity";
    public static final String ARG_articlebean = "arg_articlebean";

    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    protected XiaoYaWebView webView;
    @Bind(R.id.webview_container)
    protected FrameLayout webviewContainer;

    protected boolean isCollected;

    protected MenuItem mMenuItem;

    private BrowserBottomPopupwindow mPopupWindow;
    @Inject
    CollectionAndLikePresenter collectionPresenter;
    private MeishiBean content;

    public void onReloadClick() {
        if((null != content)) {
            webView.reload();
        }
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.content = (MeishiBean) getIntent().getSerializableExtra(ARG_articlebean);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
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
        return R.layout.act_wechatarticle_detail;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.tabbar_close_icon2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("init webview ......");
                webView = new XiaoYaWebView(getApplicationContext());
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

                webView.loadUrl(content.getArticleUrl());

                initPopupWindow();
            }
        });
    }

    /**
     * 初始化popupwindow
     */
    private void initPopupWindow() {
        mPopupWindow = new BrowserBottomPopupwindow(WechatArticleDetailActivity.this);
        mPopupWindow.setBrowserPopupwindowListener(this);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mPopupWindow.setLightValue(WindowUtils.getScreenBrightness(WechatArticleDetailActivity.this));

    }

    private void initFontEvent() {
        mPopupWindow.setAnimationStyle(R.style.anim_dir_popwindow);
        mPopupWindow.showAtLocation(webviewContainer, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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
        if(null != collectionPresenter) {
            collectionPresenter.attachView(this);
        }
    }

    /**
     * 监听用户点击物理返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articlebean_shareable_detail, menu);
        mMenuItem = menu.findItem(R.id.menu_collect);

        checkCollected();

        updateCollectionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void collectOrUnCollect() {
        if (isCollected) {
            content.setIsCollected(false);
        } else {
            content.setIsCollected(true);
        }
        collectionPresenter.collectOrUnCollect(content);
    }

    private void checkCollected() {
        collectionPresenter.checkIfCollected(content);
    }

    public void updateCollectionsMenu() {
        if(isCollected){
            mMenuItem.setIcon(R.drawable.tabbar_collect_icon_press);
        }else {
            mMenuItem.setIcon(R.drawable.tabbar_collect_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_share) {
//            initShareEvent();
        } else if(item.getItemId() == R.id.menu_font) {
            initFontEvent();
        } else if(item.getItemId() == R.id.menu_collect) {
            collectOrUnCollect();
        }

        return true;
    }

    /**
     * 处理分享事件
     *//*
    private void initShareEvent() {
        UMImage image = new UMImage(WechatArticleDetailActivity.this, content.getImageUrls());
        new ShareAction(this).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA
                ,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .withTitle("来自小亚的分享")
                .withText(content.getTitle())
                .withMedia(image)
                .withTargetUrl(content.getArticleUrl())
                .setCallback(umShareListener)
                //.withShareBoardDirection(view, Gravity.TOP|Gravity.LEFT)
                .open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("platform"+platform);
            if("WEIXIN_FAVORITE".equals(platform.name())){
                Toast.makeText(WechatArticleDetailActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WechatArticleDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WechatArticleDetailActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            LogUtils.d(platform+"  分享失败啦 ");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WechatArticleDetailActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        LogUtils.d("onActivityResult");
    }

    @Override
    protected void onDestroy() {
        if(null != collectionPresenter) {
            collectionPresenter.detachView();
            this.collectionPresenter = null;
        }
        if(webView!=null) {
            webviewContainer.removeView(webView);

            webView.removeCallBack();
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }

//        umShareListener = null;
        content = null;
        if(progress != null) {
            progress = null;
        }
        if(webviewContainer != null) {
            webviewContainer = null;
        }

        mPopupWindow = null;

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        /*MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mAppContext);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView != null) {
            webView.onResume();
        }

        /*MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mAppContext);*/
    }


    @Override
    public void lightOnclick(int progress) {
        WindowUtils.setScreenBrightness(WechatArticleDetailActivity.this,progress);
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
    public void loadError(int loadType, AppException e) {
        LogUtils.d(e);

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
