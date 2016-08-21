package com.wanghaisheng.weiyang.ui.poi;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.Path;
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
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiDetailPresenter;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiDetailView;
import com.wanghaisheng.weiyang.utils.AMapLocationUtil;
import com.wanghaisheng.weiyang.utils.amap.AMapNavUtil;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/16 11:49
 * Email: 1392100700@qq.com
 */
public class PoiDetailActivity extends BaseActivity implements AMapPoiDetailView,AMapLocationUtil.MyLocationListener {

    public static final String POI_ID = "poi_id";

    private AMapLocationUtil aMapLocationUtil;

    //当前位置的坐标
    private LatLonPoint mMyLatLng;
    //餐厅位置的坐标
    private LatLonPoint mThisLatLng;
    //当前所在的城市
    private String mCurrentCity;

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
    @Bind(R.id.iv_people_tips)
    ImageView ivPeopleTip;

    private MapPoiBean mapPoiBean;
    private MapPoiDetailBean mapPoiDetailBean;

    @Inject
    AMapPoiDetailPresenter presenter;
    //已经执行过路线规划
    private boolean navSetuped;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(presenter != null) {
            presenter.attachView(this);
            presenter.getMapPoiDetail(mapPoiBean);
        }

        aMapLocationUtil = new AMapLocationUtil(getApplicationContext(),this);
        aMapLocationUtil.startLocation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_poi_detail;
    }


    @OnClick({R.id.cv_intro,R.id.cv_nav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_nav:
                Navigator.openAMapRouteActivity(PoiDetailActivity.this,mMyLatLng,mThisLatLng,mCurrentCity);
                break;

        }

    }

    @Override
    public void renderAMapPoiDetail(MapPoiDetailBean detailBean) {
        this.mapPoiDetailBean = detailBean;

        if(!TextUtils.isEmpty(detailBean.getCoverImg())) {
            mdvCover.setDraweeViewUrl(detailBean.getCoverImg());
        }
//        getSupportActionBar().setTitle(detailBean.getName());
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
        mThisLatLng = new LatLonPoint(detailBean.getLatitude(),detailBean.getLongitude());
        setupDistanceAndNav();

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

    @Override
    public void myLocation(AMapLocation mapLocation) {
        if(mapLocation != null && mapLocation.getErrorCode() == 0) {
            this.mCurrentCity = mapLocation.getCity();

            this.mMyLatLng = new LatLonPoint(mapLocation.getLatitude(),mapLocation.getLongitude());
            setupDistanceAndNav();
        }
    }

    //设置距离和导航
    private void setupDistanceAndNav() {
        if(mMyLatLng != null && mThisLatLng != null && !navSetuped) {
            // 计算量坐标点距离
            int distance = (int) AMapUtils.calculateLineDistance(AMapUtil.convertToLatLng(mMyLatLng), AMapUtil.convertToLatLng(mThisLatLng));
            AMapNavUtil navUtil = new AMapNavUtil(getApplicationContext());

            int routeType = AMapNavUtil.ROUTE_TYPE_WALK;
            if(distance > 1000) {
                routeType = AMapNavUtil.ROUTE_TYPE_DRIVE;
            }
            navUtil.getDistanceAndDuration(mMyLatLng, mThisLatLng, mapPoiBean.getCityname(), routeType, new AMapNavUtil.NavPlanResultListener() {
                @Override
                public void navPlanResult(Path routePath, int routeType) {
                    String time = AMapUtil.getFriendlyTime((int) routePath.getDuration());
                    tvGoTime.setText(time);
                    String dis = AMapUtil.getFriendlyLength((int) routePath.getDistance());
                    tvNavDistance.setText(dis);
                    tvDistance.setText(dis);

                    if(routeType == AMapNavUtil.ROUTE_TYPE_DRIVE) {
                        ivPeopleTip.setImageResource(R.drawable.drive_tips);
                    }
                }
            });
            navSetuped = true;
        }
    }


}
