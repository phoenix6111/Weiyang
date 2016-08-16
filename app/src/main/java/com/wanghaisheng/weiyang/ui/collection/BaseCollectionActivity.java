package com.wanghaisheng.weiyang.ui.collection;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.R;

import butterknife.Bind;

/**
 * Created by sheng on 2016/4/19.
 */
public class BaseCollectionActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    public void getDatas(Bundle savedInstanceState) {

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

    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_appbar_content;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        tvTitle.setText("收藏内容");
    }

    @Override
    public void initData() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content,CollectionTopNavigationFragment.newInstance()).commit();
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }
}
