package com.wanghaisheng.weiyang.utils.amap;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.Path;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.wanghaisheng.weiyang.R;

/**
 * Author: sheng on 2016/8/17 12:39
 * Email: 1392100700@qq.com
 * 导航工具类
 */
public class AMapNavUtil implements RouteSearch.OnRouteSearchListener {

    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;

    private LatLonPoint mStartPoint;//起点，
    private LatLonPoint mEndPoint;//终点，
    private String mCurrentCity;

    private int routeType;
    public static final int ROUTE_TYPE_BUS = 1;
    public static final int ROUTE_TYPE_DRIVE = 2;
    public static final int ROUTE_TYPE_WALK = 3;

    private NavPlanResultListener planResultListener;

    public void setPlanResultListener(NavPlanResultListener planResultListener) {
        this.planResultListener = planResultListener;
    }

    public AMapNavUtil(Context mContext) {
        this.mContext = mContext;

        mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     * @param startPoint
     * @param endPoint
     * @param routeType
     */
    public void getDistanceAndDuration(LatLonPoint startPoint,LatLonPoint endPoint,String city,int routeType) {
        this.mStartPoint = startPoint;
        this.mEndPoint = endPoint;
        this.mCurrentCity = city;
    }

    public void getDistanceAndDuration(LatLonPoint startPoint,LatLonPoint endPoint,String city,int routeType,NavPlanResultListener planResultListener) {
        this.mStartPoint = startPoint;
        this.mEndPoint = endPoint;
        this.mCurrentCity = city;
        this.routeType = routeType;
        this.planResultListener = planResultListener;
        searchRouteResult(routeType);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {

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

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType) {

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusDefault,
                    mCurrentCity, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询

        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    final BusPath drivePath = mBusRouteResult.getPaths().get(0);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    if(planResultListener != null) {
                        Path path = new Path();
                        path.setDistance(dis);
                        path.setDuration(dur);
                        planResultListener.navPlanResult(path,ROUTE_TYPE_BUS);
                    }
                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, errorCode);
        }

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    if(planResultListener != null) {
                        Path path = new Path();
                        path.setDistance(dis);
                        path.setDuration(dur);
                        planResultListener.navPlanResult(path,ROUTE_TYPE_DRIVE);
                    }
                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);

                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    if(planResultListener != null) {
                        Path path = new Path();
                        path.setDistance(dis);
                        path.setDuration(dur);
                        planResultListener.navPlanResult(path,ROUTE_TYPE_WALK);
                    }
                } else if (result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, errorCode);
        }
    }

    public interface NavPlanResultListener {
        void navPlanResult(Path routePath,int routeType);
    }
}
