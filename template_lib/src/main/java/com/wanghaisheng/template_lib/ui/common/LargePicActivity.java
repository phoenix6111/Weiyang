package com.wanghaisheng.template_lib.ui.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.navigator.BaseNavigator;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.FileHelper;

import java.util.ArrayList;


/**
 * Created by sheng on 2016/5/8.
 */
public class LargePicActivity extends BaseActivity {
    private static final String TAG = "LargePicActivity";
    public static final String ARG_INEX = "arg_index";
    public static final String ARG_URLS = "arg_urls";

    private int index;
    private ArrayList<String> urls = new ArrayList<>();

    Toolbar mToolbar;
    ViewPager viewPager;
    FragmentStatePagerAdapter pagerAdapter;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        Intent intent = getIntent();
        this.index = intent.getIntExtra(ARG_INEX,0);
        this.urls = intent.getStringArrayListExtra(ARG_URLS);
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
    protected int getLayoutId() {
        return R.layout.act_large_pic;
    }

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);

//        supportPostponeEnterTransition();//延缓执行 然后在fragment里面的控件加载完成后start
    }

    @Override
    public void initData() {
//        LogUtils.d("print urls..............");
//        LogUtils.d(urls);
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return LargePicFragment.newInstance(position,urls.get(position));
            }

            @Override
            public int getCount() {
                return urls.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);

    }

    @Override
    protected void initInjector() {

    }

    @TargetApi(22)
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(ARG_INEX, viewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_largepic, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_largepic) {
            saveImage();
        }

        return true;
    }

    private void saveImage() {
        if(!FileHelper.hasSDCard()) {
            Toast.makeText(getApplicationContext(), "未安装内存卡，不能下载", Toast.LENGTH_SHORT).show();
        } else {
            //下载图片
            String currentImage = urls.get(viewPager.getCurrentItem());
            ArrayList<String> url = new ArrayList<>();
            url.add(currentImage);
            BaseNavigator.startDownloadImageService(LargePicActivity.this,url);
        }
    }

    public void showOrHideToolbar() {
        float height = mToolbar.getY();
        if(height<0) {
            showToolbar();
        } else {
            hideToolbar();
        }
    }

    public void hideToolbar() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void showToolbar() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

}
