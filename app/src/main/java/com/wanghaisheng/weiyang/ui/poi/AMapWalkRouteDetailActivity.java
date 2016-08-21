package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/19 16:18
 * Email: 1392100700@qq.com
 */
public class AMapWalkRouteDetailActivity extends BaseActivity {

    public static final String ARG_WALK_PATH = "arg_walk_path";
    public static final String ARG_DEST_ADDR = "arg_dest_addr";

    private WalkPath mWalkPath;
    //目的地
    private String mDestAddr;

    private Context mContext;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.iv_dest_addr)
    TextView tvDestAddr;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<WalkStep> mDatas;
    private CommonAdapter<WalkStep> mAdapter;

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        super.initView();

        this.mContext = getApplicationContext();

        initToolbar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvDestAddr.setText(mDestAddr);
        String duration = AMapUtil.getFriendlyTime((int) mWalkPath.getDuration());
        tvDuration.setText(duration);
        String distance = AMapUtil.getFriendlyLength((int) mWalkPath.getDistance());
        tvDistance.setText(distance);

        mDatas = mWalkPath.getSteps();
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = initAdapter();
        recyclerview.setAdapter(mAdapter);
    }

    private CommonAdapter<WalkStep> initAdapter() {
        return new CommonAdapter<WalkStep>(mContext,R.layout.item_amap_route_walk,mDatas) {
            @Override
            public void convert(ViewHolder holder, WalkStep walkStep, int position) {
                if(position == 0) {
                    holder.setImageResource(R.id.iv_route_dir,R.drawable.action_foot_navi_start);
                    holder.setText(R.id.iv_route_instruction,"从我的位置 "+walkStep.getInstruction());
                } else if(position == mDatas.size()-1) {
                    holder.setImageResource(R.id.iv_route_dir,R.drawable.action_foot_navi_end);
                    holder.setText(R.id.iv_route_instruction,"到达 "+" 川香石锅鱼");
                } else {
                    int resId = AMapUtil.getWalkActionID(walkStep.getAction());
                    holder.setImageResource(R.id.iv_route_dir,resId);
                    holder.setText(R.id.iv_route_instruction,walkStep.getInstruction());
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_amap_walk_route_detail;
    }


    @OnClick(R.id.btn_start_nav)
    public void onClick() {
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        super.getDatas(savedInstanceState);
        this.mDestAddr = getIntent().getStringExtra(ARG_DEST_ADDR);
        this.mWalkPath = getIntent().getParcelableExtra(ARG_WALK_PATH);
    }
}
