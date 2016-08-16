package com.wanghaisheng.template_lib.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.StringHelper;
import com.wanghaisheng.template_lib.widget.XiaoYaWebView;



/**
 * Created by sheng on 2016/4/21.
 */
public class BrowserActivity extends BaseActivity {
    public static final String ARG_URL = "url";

    ProgressBar progress;
    Toolbar mToolbar;
    TextView tvTitle;

    protected XiaoYaWebView webView;
    protected FrameLayout webviewContainer;

    StringHelper mStringHelper;

    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.act_brower_layout;
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.url = getIntent().getStringExtra(ARG_URL);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeGroup(R.id.browser);
        getMenuInflater().inflate(R.menu.menu_browser, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            onReloadClick();
        } else if (item.getItemId() == R.id.copy) {
            mStringHelper.copy(url);
        } else if (item.getItemId() == R.id.to_browser) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onReloadClick() {
        if(!TextUtils.isEmpty(url)) {
            webView.reload();
        }
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_close_icon);

        webView = new XiaoYaWebView(BrowserActivity.this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webviewContainer.removeAllViews();
        webviewContainer.addView(webView);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
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
    }

    @Override
    public void initData() {
        if(!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();

    }

    @Override
    public void onDestroy() {
        if(webView!=null) {
            webviewContainer.removeView(webView);

            webView.removeCallBack();
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }

        if(progress != null) {
            progress = null;
        }
        if(webviewContainer != null) {
            webviewContainer = null;
        }
        super.onDestroy();
    }
}
