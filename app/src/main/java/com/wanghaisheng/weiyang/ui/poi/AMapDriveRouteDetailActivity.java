package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Author: sheng on 2016/8/18 12:15
 * Email: 1392100700@qq.com
 */
public class AMapDriveRouteDetailActivity extends BaseActivity {
    public static final String ARG_DRIVE_PATH = "ARG_DRIVE_PATH";
    public static final String ARG_DRIVE_ROUTE_RESULT = "arg_drive_route_result";
    public static final String ARG_DEST_ADDR = "arg_dest_addr";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_dur_dist)
    TextView mTvDurDist;
    @Bind(R.id.tv_route_roads)
    TextView mTvRouteRoads;
    @Bind(R.id.tv_tri_taxicost)
    TextView mTvTriTaxicost;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private List<DriveStep> mDatas = new ArrayList<>();
    private CommonAdapter<DriveStep> mAdapter;
    private Context mContext;
    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    //目的地址
    private String mDestAddr;

    @Override
    protected void initView() {
        super.initView();
        initToolbar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.mContext = getApplicationContext();
        mTvTitle.setText("我的位置 - "+mDestAddr);
        String duration = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
        String distance = AMapUtil.getFriendlyLength((int) mDrivePath.getDistance());
        mTvDurDist.setText(duration+" "+distance);
        mTvRouteRoads.setText(AMapUtil.getDrivePathTitle(mDrivePath));
        mTvTriTaxicost.setText("红绿灯"+mDrivePath.getTotalTrafficlights()+"个 "+"打车约"+(int)mDriveRouteResult.getTaxiCost()+"元");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = initAdapter();
        mDatas.clear();
        DriveStep startStep = new DriveStep();
        startStep.setRoad("从 我的位置 出发");
        mDatas.add(startStep);
        mDatas.addAll(mDrivePath.getSteps());
        DriveStep endStep = new DriveStep();
        endStep.setRoad("到达终点 "+mDestAddr);
        mDatas.add(endStep);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initInjector() {

    }

    private CommonAdapter<DriveStep> initAdapter() {
        return new CommonAdapter<DriveStep>(mContext,R.layout.item_amap_route_drive,mDatas) {
            @Override
            public void convert(final ViewHolder holder,final DriveStep driveStep, final int position) {
                LogUtils.d(driveStep);
                if(position == 0) {
                    ImageView startEndImg = holder.getView(R.id.iv_bus_route_dir_start_end);
                    startEndImg.setImageResource(R.drawable.drive_route_start);
                    startEndImg.setVisibility(View.VISIBLE);
                    holder.setVisible(R.id.iv_bus_route_dir_up,false);
                    holder.setVisible(R.id.iv_bus_route_dir_down,true);
                    holder.setVisible(R.id.iv_dir_other,false);
                    holder.setText(R.id.tv_parent_title,driveStep.getRoad());
                    holder.setVisible(R.id.tv_dis_traf,false);
                    holder.setVisible(R.id.iv_expand,false);
                    holder.setVisible(R.id.bottom_divider,false);

                } else if(position == mDatas.size()-1) {
                    ImageView startEndImg = holder.getView(R.id.iv_bus_route_dir_start_end);
                    startEndImg.setImageResource(R.drawable.drive_route_end);
                    startEndImg.setVisibility(View.VISIBLE);
                    holder.setVisible(R.id.iv_bus_route_dir_up,true);
                    holder.setVisible(R.id.iv_bus_route_dir_down,false);
                    holder.setVisible(R.id.iv_dir_other,false);
                    holder.setText(R.id.tv_parent_title,driveStep.getRoad());
                    holder.setVisible(R.id.tv_dis_traf,false);
                    holder.setVisible(R.id.iv_expand,false);

                } else {
                    holder.setVisible(R.id.iv_bus_route_dir_start_end,false);
                    holder.setVisible(R.id.iv_bus_route_dir_up,true);
                    holder.setVisible(R.id.iv_bus_route_dir_down,true);
                    ImageView ivDirOther = holder.getView(R.id.iv_dir_other);
                    String actionName = driveStep.getAction();
                    int resID = AMapUtil.getDriveActionID(actionName);
                    ivDirOther.setImageResource(resID);
                    ivDirOther.setVisibility(View.VISIBLE);

                    String road = driveStep.getRoad();
                    if(TextUtils.isEmpty(road)) {
                        road = "内部道路";
                    }
                    holder.setText(R.id.tv_parent_title,road);
                    String distance = AMapUtil.getFriendlyLength((int) driveStep.getDistance());
                    String duration = AMapUtil.getFriendlyTime((int) driveStep.getDuration());
                    holder.setText(R.id.tv_dis_traf,distance+" 预计用时"+duration);
                    holder.setVisible(R.id.tv_dis_traf,true);
                    holder.setVisible(R.id.iv_expand,true);

                    final ImageView expandImg = holder.getView(R.id.iv_expand);
                    expandImg.setVisibility(View.VISIBLE);

                    Object tag = expandImg.getTag();
                    if(tag != null && (boolean)tag) {
                        expandImg.setImageResource(R.drawable.busnavi_blue_arrow_up);
                    } else {
                        //否则设为收缩状态
                        expandImg.setImageResource(R.drawable.busnavi_blue_arrow_down);
                    }

                    final LinearLayout expandContent = holder.getView(R.id.expand_content);

                    //点击时改变expandImg的状态，显示隐藏 child
                    holder.setOnClickListener(R.id.rl_title, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Object tag = expandImg.getTag();
                            if(tag != null && (boolean)tag) {
                                //如果有值，且是展开状态，则置为收缩状态
                                expandContent.removeAllViews();
                                expandImg.setImageResource(R.drawable.busnavi_blue_arrow_down);
                                expandImg.setTag(false);
                            } else {
                                //否则设为展开状态
                                expandContent.addView(getExpandView(driveStep));
                                expandImg.setImageResource(R.drawable.busnavi_blue_arrow_up);
                                expandImg.setTag(true);
                            }
                        }
                    });
                }
            }
        };
    }

    private View getExpandView(DriveStep driveStep) {
        RelativeLayout container = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.item_amap_route_drive_expand_content,null);
        ImageView dirImg = (ImageView) container.findViewById(R.id.iv_bus_route_child_dir);
        dirImg.setImageResource(AMapUtil.getDriveActionID(driveStep.getAction()));
        TextView instruction = (TextView) container.findViewById(R.id.tv_bus_route_child_desc);
        instruction.setText(driveStep.getInstruction());

        return container;
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        super.getDatas(savedInstanceState);
        this.mDrivePath = getIntent().getParcelableExtra(ARG_DRIVE_PATH);
        this.mDriveRouteResult = getIntent().getParcelableExtra(ARG_DRIVE_ROUTE_RESULT);
        this.mDestAddr = getIntent().getStringExtra(ARG_DEST_ADDR);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_amap_drive_route_detail;
    }
}
