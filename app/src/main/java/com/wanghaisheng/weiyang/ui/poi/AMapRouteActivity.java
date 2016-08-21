package com.wanghaisheng.weiyang.ui.poi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;
import com.wanghaisheng.weiyang.utils.amap.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/17 18:08
 * Email: 1392100700@qq.com
 */
public class AMapRouteActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
    public static final String ARG_START_LATLON = "arg_start_latlon";
    public static final String ARG_END_LATLON = "arg_end_latlon";
    public static final String ARG_CITY = "arg_city";

    private Context mContext;
    private AMap mAMap;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    //起点坐标
    private LatLonPoint mStartPoint;//起点，
    //终点坐标
    private LatLonPoint mEndPoint;//终点
    private String mCurrentCity;
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;

    private ProgressDialog progDialog = null;// 搜索时进度条

    @Bind(R.id.iv_route_drive)
    ImageView ivRouteDrive;
    @Bind(R.id.iv_route_bus)
    ImageView ivRouteBus;
    @Bind(R.id.iv_route_walk)
    ImageView ivRouteWalk;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.tv_drive_duration)
    TextView tvDriveDuration;
    @Bind(R.id.tv_drive_distance)
    TextView tvDriveDistance;
    @Bind(R.id.tv_drive_total_trafficlights)
    TextView tvDriveTotalTrafficlights;
    @Bind(R.id.tv_drive_taxicost)
    TextView tvDriveTaxicost;
    @Bind(R.id.ll_drive_content)
    LinearLayout llDriveContent;
    @Bind(R.id.tv_walk_duration)
    TextView tvWalkDuration;
    @Bind(R.id.tv_walk_distance)
    TextView tvWalkDistance;
    @Bind(R.id.tv_crossroads)
    TextView tvCrossroads;
    @Bind(R.id.ll_walk_content)
    RelativeLayout llWalkContent;
    @Bind(R.id.btn_nav)
    Button btnNav;
    @Bind(R.id.rv_bus_result)
    RecyclerView busRecyclerView;
    @Bind(R.id.map_container)
    RelativeLayout rlMapContainer;

    private CommonAdapter<BusPath> busAdapter;
    private List<BusPath> busDatas = new ArrayList<>();

    @Override
    protected void initInjector() {

    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        super.getDatas(savedInstanceState);

        this.mStartPoint = getIntent().getParcelableExtra(ARG_START_LATLON);
        this.mEndPoint = getIntent().getParcelableExtra(ARG_END_LATLON);
        this.mCurrentCity = getIntent().getStringExtra(ARG_CITY);

        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this.getApplicationContext();

        init();

        setFromAndToMarker();

        rlMapContainer.setVisibility(View.VISIBLE);
        busRecyclerView.setVisibility(View.GONE);
        searchRouteResult(ROUTE_TYPE_DRIVE,RouteSearch.DrivingDefault);
    }

    private CommonAdapter<BusPath> initBusAdapter() {
        busAdapter = new CommonAdapter<BusPath>(mContext,R.layout.item_amap_route_bus,busDatas) {
            @Override
            public void convert(ViewHolder holder, final BusPath busPath, int position) {
                holder.setText(R.id.tv_bus_path_title, AMapUtil.getBusPathTitle(busPath));
                holder.setText(R.id.tv_bus_path_desc,AMapUtil.getBusPathDes(busPath));

                holder.setOnClickListener(R.id.rl_bus_path,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Navigator.openAMapBusRoutePathDetail(AMapRouteActivity.this,busPath,mBusRouteResult,"金茂园");
                    }
                });
            }
        };

        return busAdapter;
    }

    private void setFromAndToMarker() {
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble_start)));
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble_end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
        }

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

        busAdapter = initBusAdapter();
        busRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        busRecyclerView.setAdapter(busAdapter);
    }

    public void onBusClick(View view) {
        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
        ivRouteDrive.setImageResource(R.drawable.route_icon_car);
        ivRouteBus.setImageResource(R.drawable.route_icon_bus_hl);
        ivRouteWalk.setImageResource(R.drawable.route_icon_onfoot);
        rlMapContainer.setVisibility(View.GONE);
        busRecyclerView.setVisibility(View.VISIBLE);
    }

    public void onDriveClick(View view) {
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        ivRouteDrive.setImageResource(R.drawable.route_icon_car_hl);
        ivRouteBus.setImageResource(R.drawable.route_icon_bus);
        ivRouteWalk.setImageResource(R.drawable.route_icon_onfoot);
        rlMapContainer.setVisibility(View.VISIBLE);
        busRecyclerView.setVisibility(View.GONE);
    }

    public void onWalkClick(View view) {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        ivRouteDrive.setImageResource(R.drawable.route_icon_car);
        ivRouteBus.setImageResource(R.drawable.route_icon_bus);
        ivRouteWalk.setImageResource(R.drawable.route_icon_onfoot_hl);
        rlMapContainer.setVisibility(View.VISIBLE);
        busRecyclerView.setVisibility(View.GONE);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCity, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_amap_route;
    }

    @OnClick({R.id.iv_route_drive, R.id.iv_route_bus, R.id.iv_route_walk
            , R.id.ll_drive_content, R.id.ll_walk_content, R.id.btn_nav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_route_drive:
                onDriveClick(view);
                break;
            case R.id.iv_route_bus:
                onBusClick(view);
                break;
            case R.id.iv_route_walk:
                onWalkClick(view);
                break;
            case R.id.ll_drive_content:
                LogUtils.d("drive detail..");
                LogUtils.d(mDriveRouteResult);
                if(mDriveRouteResult != null) {
                    Navigator.openAMapDriveRouteDetail(AMapRouteActivity.this,mDriveRouteResult.getPaths().get(0),mDriveRouteResult,"金茂园");
                }
                break;
            case R.id.ll_walk_content:
                if(mWalkRouteResult != null) {
                    Navigator.openAMapWalkRouteDetail(AMapRouteActivity.this,mWalkRouteResult.getPaths().get(0),"金茂园");
                }
                break;
            case R.id.btn_nav:
                break;
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mAMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    busDatas.clear();
                    busDatas.addAll(mBusRouteResult.getPaths());
                    busAdapter.notifyDataSetChanged();
                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mAMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    DriveRouteColorfulOverLay drivingRouteOverlay = new DriveRouteColorfulOverLay(
                            mAMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    llDriveContent.setVisibility(View.VISIBLE);

                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String durStr = AMapUtil.getFriendlyTime(dur);
                    String disStr = AMapUtil.getFriendlyLength(dis);
                    tvDriveDuration.setText(durStr);
                    tvDriveDistance.setText(disStr);
                    int totalTrafficlights = drivePath.getTotalTrafficlights();
                    tvDriveTotalTrafficlights.setText("红绿灯"+totalTrafficlights+"个");
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    tvDriveTaxicost.setText("打车约" + taxiCost + "元");
                    llWalkContent.setVisibility(View.GONE);

                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mAMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);

                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, mAMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    llWalkContent.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    String disStr = AMapUtil.getFriendlyLength(dis);
                    tvWalkDistance.setText(disStr);
                    int dur = (int) walkPath.getDuration();
                    String durStr = AMapUtil.getFriendlyTime(dur);
                    tvWalkDuration.setText(durStr);
                    tvCrossroads.setText("共经过"+walkPath.getSteps().size()+"个十字路口");

                    llDriveContent.setVisibility(View.GONE);

                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
