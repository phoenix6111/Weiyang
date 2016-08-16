package com.wanghaisheng.weiyang.ui.poi;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.wanghaisheng.weiyang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sheng on 2016/8/14 22:45
 * Email: 1392100700@qq.com
 */
public class MyPoiOverlay2 {

    private Context mContext;

    private AMap mAMap;
    //搜索到的PoiItem
    private List<PoiItem> mPois;
    //地图上显示的marker
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    //保存点击的marker
    private Marker mlastMarker;

    private int[] markers = {
            R.drawable.poi_marker_1,
            R.drawable.poi_marker_2,
            R.drawable.poi_marker_3,
            R.drawable.poi_marker_4,
            R.drawable.poi_marker_5,
            R.drawable.poi_marker_6,
            R.drawable.poi_marker_7,
            R.drawable.poi_marker_8,
            R.drawable.poi_marker_9,
            R.drawable.poi_marker_10
    };

    public MyPoiOverlay2(Context context, AMap amap , List<PoiItem> pois) {
        this.mContext = context;
        this.mAMap = amap;
        this.mPois = pois;
    }

    /**
     * 添加所有的Marker到地图中。
     * @since V2.1.0
     */
    public void addAllToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mAMap.addMarker(getMarkerOptions(i));
            PoiItem item = mPois.get(i);
            marker.setObject(item);
            mPoiMarks.add(marker);
        }
    }

    /**
     * 去掉PoiOverlay上所有的Marker。
     *
     * @since V2.1.0
     */
    public void removeAllFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    /**
     * 移动镜头到当前的视角。
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mAMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(), mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

    /**
     * 从marker中得到poi在list的位置。
     *
     * @param marker 一个标记的对象。
     * @return 返回该marker对应的poi在list的位置。
     * @since V2.1.0
     */
    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回第index的poi的信息。
     * @param index 第几个poi。
     * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong>
     *     <a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a>
     * </strong>。
     * @since V2.1.0
     */
    public PoiItem getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }

    /**
     * 根据 index 获取 BitmapDescriptor
     * @param index
     * @return
     */
    protected BitmapDescriptor getBitmapDescriptor(int index) {
        if (index < 10) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(mContext.getResources(), markers[index]));
            return icon;
        }else {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker_other_highlight));
            return icon;
        }
    }

    // 将之前被点击的marker置为原来的状态
    public void resetLastmarker() {
        if(mlastMarker == null) {
            return;
        }

        int index = getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            mContext.getResources(),
                            markers[index])));
        }else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }

    /**
     * 设置LastMarker
     * @param marker
     */
    public void setLastMarker(Marker marker) {
        if (mlastMarker == null) {
            mlastMarker = marker;
        } else {
            // 将之前被点击的marker置为原来的状态
            resetLastmarker();
            mlastMarker = marker;
        }
    }

}
