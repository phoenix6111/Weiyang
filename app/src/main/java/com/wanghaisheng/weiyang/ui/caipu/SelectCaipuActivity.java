package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by sheng on 2016/7/4.
 */
public class SelectCaipuActivity extends BaseActivity implements SidebarItemSelectedListener {
    private static final String TAG = "SelectCaipuActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_primary_cate)
    RecyclerView primaryCateView;
    @Bind(R.id.content)
    FrameLayout secondCateContent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String[] cateArrs = {"菜式","主食","烘焙","菜系","厨房工具","烹饪方法","口味","场景","人群","美容瘦身","功效","疾病调理","节日/时令"};
    List<String> primaryCate = new ArrayList<>();
    PrimaryCateAdapter adapter;

    @Override
    public void getDatas(Bundle savedInstanceState) {

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
        return R.layout.act_meishi_select_caipu;
    }

    @Override
    public void initView() {
        initToolbar(toolbar);
        tvTitle.setText("菜谱分类");
        fragmentManager = getSupportFragmentManager();

        primaryCateView.setLayoutManager(new LinearLayoutManager(SelectCaipuActivity.this));
        primaryCate.addAll(Arrays.asList(cateArrs));
        adapter = new PrimaryCateAdapter(SelectCaipuActivity.this, R.layout.item_meishi_cate_sidebar,primaryCate);
        adapter.setSelectedListener(this);
        primaryCateView.setAdapter(adapter);

        adapter.setSelectedPosition(0);
        adapter.notifyDataSetChanged();
        onItemSelected(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onItemSelected(int pos) {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content,CaipuListFragment.newInstance(pos));

        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meishi_caipu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_search) {
            Navigator.openMeishiSearchActivity(SelectCaipuActivity.this);
        }

        return true;
    }

}
