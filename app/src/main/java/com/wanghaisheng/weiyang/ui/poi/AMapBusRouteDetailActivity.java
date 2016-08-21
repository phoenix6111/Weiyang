package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Author: sheng on 2016/8/20 01:46
 * Email: 1392100700@qq.com
 */
public class AMapBusRouteDetailActivity extends BaseActivity {

    public static final String ARG_DEST_ADDR = "arg_dest_addr";
    public static final String ARG_BUS_PATH = "arg_bus_path";
    public static final String ARG_BUS_ROUTE_RESULT = "arg_bus_route_result";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_bus_route_line)
    TextView tvBusRouteLine;
    @Bind(R.id.tv_dur_cost_dis)
    TextView tvDurCostDis;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<SchemeBusStep> mBusStepList = new ArrayList<>();
    private BusRouteDetailAdapter mAdapter;

    private Context mContext;

    //bus 路线
    private BusPath mBusPath;
    private BusRouteResult mBusRouteResult;
    //目的地
    private String mDestAddr;

    @Override
    protected void initView() {
        super.initView();
        mContext = getApplicationContext();

        initToolbar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle.setText("公交路线详情");
        tvBusRouteLine.setText(AMapUtil.getBusPathTitle(mBusPath));
        tvDurCostDis.setText(AMapUtil.getBusPathDes(mBusPath));

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(new BusRouteDetailAdapter(mContext,mBusStepList,mDestAddr));

    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        super.getDatas(savedInstanceState);
        Intent intent = getIntent();
        this.mBusRouteResult = intent.getParcelableExtra(ARG_BUS_ROUTE_RESULT);
        this.mBusPath = intent.getParcelableExtra(ARG_BUS_PATH);
        this.mDestAddr = intent.getStringExtra(ARG_DEST_ADDR);

        initDatas();
    }

    /**
     * 获取公交换乘数据
     */
    private void initDatas() {
        List<BusStep> list = mBusPath.getSteps();
        SchemeBusStep start = new SchemeBusStep(null);
        start.setStart(true);
        mBusStepList.add(start);
        for (BusStep busStep : list) {
            if (busStep.getWalk() != null) {
                SchemeBusStep walk = new SchemeBusStep(busStep);
                walk.setWalk(true);
                mBusStepList.add(walk);
            }
            if (busStep.getBusLine() != null) {
                SchemeBusStep bus = new SchemeBusStep(busStep);
                bus.setBus(true);
                mBusStepList.add(bus);
            }
        }
        SchemeBusStep end = new SchemeBusStep(null);

        end.setEnd(true);
        mBusStepList.add(end);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_amap_bus_route_detail;
    }


}
