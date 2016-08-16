package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;

import java.util.List;

/**
 * Author: sheng on 2016/8/14 17:21
 * Email: 1392100700@qq.com
 */
public class PoiSearchHandler implements AMap.OnMapClickListener, AMap.InfoWindowAdapter,
        PoiSearch.OnPoiSearchListener {

    private Context mContext;
    private AMap mAMap;

    // poi返回的结果
    private PoiResult mPoiResult;
    // Poi查询条件类
    private PoiSearch.Query mQuery;

    //根据该经度查询
    private double mLatitude;
    //根据该纬度查询
    private double mLongitude;
    //当前定位的city
    private String mCity = "深圳";
    //查询的page
    private int mCurrentPage = 1;

    private List<MapPoiBean> mPoiItems;// poi数据

    private MyPoiOverlay mMyPoiOverlay;

    PoiSearchResultListener mSearchResultListener;

    public PoiSearchHandler(Context mContext, AMap mAMap) {
        this.mContext = mContext;
        this.mAMap = mAMap;

        mAMap.setOnMapClickListener(this);
//        mAMap.setOnMarkerClickListener(this);
        mAMap.setInfoWindowAdapter(this);
    }

    public void setSearchResultListener(PoiSearchResultListener searchResultListener) {
        this.mSearchResultListener = searchResultListener;
    }

    /**
     * 开始进行poi搜索
     */
    public void doSearchQuery() {
        mQuery = new PoiSearch.Query("", "餐饮服务", mCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(mCurrentPage);// 设置查第一页

        //构造搜索
        PoiSearch poiSearch = new PoiSearch(mContext, mQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mLatitude,mLongitude), MeishiMapFragment.POI_SEARCH_AREA, true));//
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LogUtils.d(marker);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LogUtils.d(marker);
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //点击地图时，重置点击的marker
        if(mMyPoiOverlay != null) {
            mMyPoiOverlay.resetLastmarker();
        }
    }

    public void onMarkerClick(Marker marker) {
        LogUtils.d(marker);
        if (marker.getObject() != null) {
            try {
                if(mMyPoiOverlay != null) {
                    mMyPoiOverlay.setLastMarker(marker);
                }
                marker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                mContext.getResources(),
                                R.drawable.poi_marker_pressed)));
                LogUtils.d("set poiitemdisplaycontent  ");
            } catch (Exception e) {
                LogUtils.d(e);
            }
        }else {
            if(mMyPoiOverlay != null) {
                mMyPoiOverlay.resetLastmarker();
            }
        }

    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(mQuery)) {// 是否是同一条


                }
            } else {
                ToastUtil.showCenterToast(mContext, R.string.no_result);
            }
        }
    }

    public void renderPoiResultToMap(List<MapPoiBean> poiItems) {
        mPoiItems = poiItems;
        //清除POI信息显示
//                        whetherToShowDetailInfo(false);
        //清理之前搜索结果的marker，还原点击marker样式
        if (mMyPoiOverlay !=null) {
            mMyPoiOverlay.resetLastmarker();
            mMyPoiOverlay.removeAllFromMap();
        }
        mAMap.clear();
        mMyPoiOverlay = new MyPoiOverlay(mContext,mAMap, mPoiItems);
        mMyPoiOverlay.addAllToMap();
        mMyPoiOverlay.zoomToSpan();

                        /*mAMap.addMarker(new MarkerOptions()
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory
                                        .fromBitmap(BitmapFactory.decodeResource(
                                                getResources(), R.drawable.point4)))
                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
*/
        mAMap.addCircle(new CircleOptions()
                .center(new LatLng(mLatitude,
                        mLongitude)).radius(MeishiMapFragment.POI_SEARCH_AREA)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(50, 1, 1, 1))
                .strokeWidth(2));

    }

    private String logPoiItem(PoiItem poiItem) {
        String str = "getAdCode " +poiItem.getAdCode()+", getAdName "+poiItem.getAdName()+", getBusinessArea"+poiItem.getBusinessArea()+" ,id "+poiItem.getPoiId()
                +"\n, getCityCode "+poiItem.getCityCode()+", getCityName "+poiItem.getCityName()+", getDirection "+poiItem.getDirection()+", getDistance "+poiItem.getDistance()
                +"\n ,getEmail "+poiItem.getEmail()+" ,getEnter "+poiItem.getEnter()+" ,getExit "+poiItem.getExit()+" ,getIndoorData "+poiItem.getIndoorData()
                +"\n,getLatLonPoint "+poiItem.getLatLonPoint()+" ,getParkingType "+poiItem.getParkingType()+" ,getPoiId "+poiItem.getPoiId()+" ,getPostcode "+poiItem.getPostcode()
                +" \n,getProvinceCode "+poiItem.getProvinceCode()+" ,getProvinceName "+poiItem.getProvinceName()+" ,getSnippet "+poiItem.getSnippet()+" ,getTel "+poiItem.getTel()
                +" \n,getTitle "+poiItem.getTitle()+" ,getTypeDes "+poiItem.getTypeDes()+" ,getWebsite"+poiItem.getWebsite();

        return str;
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }

    public void setmCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public PoiResult getPoiResult() {
        return mPoiResult;
    }

    public int getLastMarkerPosition() {
        return mMyPoiOverlay.getLastMarkerPosition();
    }

    public void onDestroy() {
        this.mSearchResultListener = null;
        this.mContext = null;
    }
}
