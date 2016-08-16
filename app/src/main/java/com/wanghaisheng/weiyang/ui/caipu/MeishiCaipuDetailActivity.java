package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by sheng on 2016/7/5.
 */
public class MeishiCaipuDetailActivity extends BaseActivity {
    public static final String ARG_CAIPU = "arg_caipu";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private String caipu;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.caipu = getIntent().getStringExtra(ARG_CAIPU);
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
        return R.layout.common_appbar_content;
    }

    @Override
    public void initView() {
        initToolbar(toolbar);
        tvTitle.setText(caipu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ZhuantiChannelEntity channelEntity = buildChannelEntity();
        transaction.replace(R.id.content, MeishiNoLazyListFragment.newInstance(channelEntity));
        transaction.commit();
    }

    private ZhuantiChannelEntity buildChannelEntity() {
        ZhuantiChannelEntity channelEntity = new ZhuantiChannelEntity();
        channelEntity.setChannel_tag("zhuanti_module_meishi_caipu_content");
        channelEntity.setChannel_title("美食");
        List<ChannelEntity> channelEntities = new ArrayList<>();
        ChannelEntity entity = new ChannelEntity();
        entity.setChannelIdentity("douguo|caipu|"+caipu);
        entity.setChannelTitle(caipu);
        entity.setModuleTitle("美食菜谱");
        channelEntities.add(entity);
        channelEntity.setChannelEntities(channelEntities);

        return channelEntity;
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meishi_caipu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_search) {
            Navigator.openMeishiSearchActivity(MeishiCaipuDetailActivity.this);
        }

        return true;
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
