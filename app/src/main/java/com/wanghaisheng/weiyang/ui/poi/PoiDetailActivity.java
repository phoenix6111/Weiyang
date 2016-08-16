package com.wanghaisheng.weiyang.ui.poi;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiDetailPresenter;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiDetailView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/16 11:49
 * Email: 1392100700@qq.com
 */
public class PoiDetailActivity extends BaseActivity implements AMapPoiDetailView{

    public static final String POI_ID = "poi_id";

    @Bind(R.id.mdv_cover)
    MySimpleDraweeView mdvCover;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.srb_score)
    SimpleRatingBar srbScore;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_classify)
    TextView tvClassify;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_tel)
    TextView tvTel;
    @Bind(R.id.cv_command_food)
    CardView cvCommandFood;
    @Bind(R.id.tv_recommend_food)
    TextView tvRecommendFood;
    @Bind(R.id.tv_business_hours)
    TextView tvBusinessHours;
    @Bind(R.id.cv_comment)
    CardView cvComment;
    @Bind(R.id.tv_comment_author1)
    TextView tvCommentAuthor1;
    @Bind(R.id.srb_comment1)
    SimpleRatingBar srbComment1;
    @Bind(R.id.tv_comment_content1)
    TextView tvCommentContent1;
    @Bind(R.id.tv_comment_date1)
    TextView tvCommentDate1;
    @Bind(R.id.tv_comment_author2)
    TextView tvCommentAuthor2;
    @Bind(R.id.srb_comment2)
    SimpleRatingBar srbComment2;
    @Bind(R.id.tv_comment_content2)
    TextView tvCommentContent2;
    @Bind(R.id.tv_comment_date2)
    TextView tvCommentDate2;
    @Bind(R.id.tv_go_time)
    TextView tvGoTime;
    @Bind(R.id.tv_nav_distance)
    TextView tvNavDistance;
    @Bind(R.id.cv_nav)
    CardView cvNav;

    private MapPoiBean mapPoiBean;

    @Inject
    AMapPoiDetailPresenter presenter;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        super.getDatas(savedInstanceState);
        this.mapPoiBean = (MapPoiBean) getIntent().getSerializableExtra(POI_ID);
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((AppContext) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar(toolbar);
        getSupportActionBar().setTitle("");
        if(presenter != null) {
            presenter.attachView(this);
            presenter.getMapPoiDetail(mapPoiBean);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_poi_detail;
    }


    @OnClick(R.id.cv_intro)
    public void onClick() {


    }

    @Override
    public void renderAMapPoiDetail(MapPoiDetailBean detailBean) {
        if(!TextUtils.isEmpty(detailBean.getCoverImg())) {
            mdvCover.setDraweeViewUrl(detailBean.getCoverImg());
        }
        getSupportActionBar().setTitle(detailBean.getName());
        tvTitle.setText(detailBean.getName());
        srbScore.setRating(detailBean.getScore());
        LogUtils.d("  price  "+detailBean.getPrice());
        if(!TextUtils.isEmpty(detailBean.getPrice())&& !"0".equals(detailBean.getPrice())) {
            tvPrice.setText("人均￥"+detailBean.getPrice()+"起");
        }
        tvClassify.setText(detailBean.getClassify());
        if(!TextUtils.isEmpty(detailBean.getDistance())) {
            tvDistance.setText(detailBean.getDistance()+"米");
        }
        tvAddress.setText(detailBean.getAddress());
        tvTel.setText(detailBean.getTel());
        if(TextUtils.isEmpty(detailBean.getTags())) {
            cvCommandFood.setVisibility(View.GONE);
        } else {
            tvRecommendFood.setText(detailBean.getTags());
        }
        if(!TextUtils.isEmpty(detailBean.getOpentime())) {
            tvBusinessHours.setText("营业时间 "+detailBean.getOpentime());
        }

        if(ListUtils.isEmpty(detailBean.getList())) {
            cvComment.setVisibility(View.GONE);
        } else {
            MapPoiDetailBean.CommentBean commentBeen = detailBean.getList().get(0);
            tvCommentAuthor1.setText(commentBeen.getAuthor());
            if(!TextUtils.isEmpty(commentBeen.getScore())) {
                srbComment1.setRating(Float.parseFloat(commentBeen.getScore()));
            }
            if(!TextUtils.isEmpty(commentBeen.getContent())) {
                tvCommentContent1.setText(Html.fromHtml(commentBeen.getContent()));
            }
            tvCommentDate1.setText(commentBeen.getTime());

            if(detailBean.getList().size()>1) {
                commentBeen = detailBean.getList().get(1);
                tvCommentAuthor2.setText(commentBeen.getAuthor());
                if(!TextUtils.isEmpty(commentBeen.getScore())) {
                    srbComment2.setRating(Float.parseFloat(commentBeen.getScore()));
                }
                if(!TextUtils.isEmpty(commentBeen.getContent())) {
                    tvCommentContent2.setText(Html.fromHtml(commentBeen.getContent()));
                }
                tvCommentDate2.setText(commentBeen.getTime());
            }

        }

        tvNavDistance.setText(detailBean.getDistance());

        /*
        @Bind(R.id.tv_go_time)
        TextView tvGoTime;
        @Bind(R.id.tv_nav_distance)
        TextView tvNavDistance;*/

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void loadError(int loadType, AppException e) {
        LogUtils.d(e);
    }
}
