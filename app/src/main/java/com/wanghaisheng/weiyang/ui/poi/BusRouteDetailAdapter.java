package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemCommonAdapter;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemTypeSupport;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.utils.amap.AMapUtil;

import java.util.List;

/**
 * Author: sheng on 2016/8/20 11:36
 * Email: 1392100700@qq.com
 */
public class BusRouteDetailAdapter extends MultiItemCommonAdapter<SchemeBusStep> {
    //布局类型：起点或终点
    public static final int ITEM_TYPE_HEAER_FOOTER = 0x001;
    //步行
    public static final int ITEM_TYPE_WALK = 0x002;
    //公交
    public static final int ITEM_TYPE_BUS = 0x003;

    private Context mContext;
    //目的地
    private String mDestAddr;

    private List<SchemeBusStep> mBusStepList;

    public BusRouteDetailAdapter(Context context, final List<SchemeBusStep> mDatas,String destAddr) {
        super(context, mDatas, new MultiItemTypeSupport<SchemeBusStep>() {
            @Override
            public int getLayoutId(int itmType) {
                switch (itmType) {
                    case ITEM_TYPE_HEAER_FOOTER:
                        return R.layout.item_amap_route_bus_detail_startend ;
                    case ITEM_TYPE_WALK:
                        return R.layout.item_amap_route_bus_detail_walk;
                    case ITEM_TYPE_BUS:
                        return R.layout.item_amap_route_bus_detail_bus;
                }

                return 0;
            }

            @Override
            public int getItemViewType(int position, SchemeBusStep schemeBusStep) {
                //第一个或最后一个
                if(position == 0 || position == mDatas.size()-1) {
                    return ITEM_TYPE_HEAER_FOOTER;
                }

                //步行
                if(schemeBusStep.isWalk() && schemeBusStep.getWalk() != null) {
                    return ITEM_TYPE_WALK;
                }

                //bus
                if(schemeBusStep.isBus() && schemeBusStep.getBusLines().size()>0) {
                    return ITEM_TYPE_BUS;
                }

                return 0;
            }
        });
        this.mContext = context;
        this.mDestAddr = destAddr;
        this.mBusStepList = mDatas;
    }

    @Override
    public void convert(ViewHolder holder, SchemeBusStep schemeBusStep, int position) {
        switch (holder.getLayoutId()) {
            case R.layout.item_amap_route_bus_detail_startend:
                setupHeaderAndFooter(holder,schemeBusStep,position);
                break;
            case R.layout.item_amap_route_bus_detail_walk:
                setupWalk(holder,schemeBusStep,position);
                break;
            case R.layout.item_amap_route_bus_detail_bus:
                setupBus(holder,schemeBusStep,position);
                break;
        }
    }

    /**
     * 设置header 和 footer
     */
    private void setupHeaderAndFooter(ViewHolder holder, SchemeBusStep schemeBusStep, int position) {
        if(position == 0) {
            holder.setText(R.id.tv_title,"我的位置出发");
            holder.setImageResource(R.id.iv_start_end,R.drawable.direction_bus_list_start);
        } else if(position == mDatas.size()-1) {
            holder.setText(R.id.tv_title,"到达终点 "+mDestAddr);
            holder.setImageResource(R.id.iv_start_end,R.drawable.direction_bus_list_end);
        }
    }

    /**
     * 设置步行方式
     */
    private void setupWalk(ViewHolder holder, SchemeBusStep schemeBusStep, int position) {
        holder.setText(R.id.tv_walk_dist_dur,"步行"+(int)schemeBusStep.getWalk().getDistance()+"米 约"
                + AMapUtil.getFriendlyTime((int) schemeBusStep.getWalk().getDuration()));
    }

    /**
     * 设置bus方式
     */
    private void setupBus(final ViewHolder holder, final SchemeBusStep schemeBusStep, int position) {
        //该公交名称
        holder.setText(R.id.tv_route_bus_name,schemeBusStep.getBusLines().get(0).getBusLineName());
        //设置该step公交换乘的起点站
        holder.setText(R.id.tv_bus_start_station,schemeBusStep.getBusLines().get(0).getDepartureBusStation().getBusStationName());
        //设置该step公交换乘的终点站
        holder.setText(R.id.tv_bus_end_station,schemeBusStep.getBusLines().get(0).getArrivalBusStation().getBusStationName());
        //该step所在经过的站数
        holder.setText(R.id.tv_bus_station_count,schemeBusStep.getBusLines().get(0).getPassStationNum()+"站");
        //设置公交线路的运营时间
        holder.setText(R.id.tv_bus_line_operate_time,"约"+AMapUtil.getFriendlyTime((int) schemeBusStep.getBusLines().get(0).getDuration()));
        final ImageView expandImg = holder.getView(R.id.iv_expand);
        Object tag = expandImg.getTag();
        if(tag != null && (boolean)tag) {
            expandImg.setImageResource(R.drawable.busnavi_blue_arrow_up);
        } else {
            //否则设为收缩状态
            expandImg.setImageResource(R.drawable.busnavi_blue_arrow_down);
        }
        holder.setOnClickListener(R.id.rl_expandable, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Object tag = expandImg.getTag();
                if(tag != null && (boolean)tag) {
                    //如果有值，且是展开状态，则置为收缩状态
                    LinearLayout expandContainer = holder.getView(R.id.rl_expand_content);
                    expandContainer.removeAllViews();
                    holder.setImageResource(R.id.iv_expand,R.drawable.busnavi_blue_arrow_down);
                    expandImg.setTag(false);
                } else {
                    //否则设为展开状态
                    addExpandContent(holder,schemeBusStep);
                    holder.setImageResource(R.id.iv_expand,R.drawable.busnavi_blue_arrow_up);
                    expandImg.setTag(true);
                }

            }
        });
    }

    private void addExpandContent(ViewHolder holder,SchemeBusStep busStep) {

        LinearLayout expandContainer = holder.getView(R.id.rl_expand_content);
        for (BusStationItem station : busStep.getBusLine()
                .getPassStations()) {
            expandContainer.addView(addBusStation(station));
        }
    }

    private LinearLayout addBusStation(BusStationItem station) {
        LinearLayout ll = (LinearLayout) View.inflate(mContext,
                R.layout.item_amap_route_bus_detail_expand_content, null);
        TextView tv = (TextView) ll
                .findViewById(R.id.tv_bus_expand_station);
        tv.setText(station.getBusStationName());

        return ll;
    }


}
