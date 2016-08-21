package com.wanghaisheng.weiyang.ui.poi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.Marker;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.ui.base.BaseViewPagerLazyFragment;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiListPresenter;
import com.wanghaisheng.weiyang.presenter.amap.AMapPoiListView;
import com.wanghaisheng.weiyang.ui.MainActivity;
import com.wanghaisheng.weiyang.ui.popwindow.PoiListPopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/14 09:39
 * Email: 1392100700@qq.com
 */
public class MeishiMapFragment extends BaseViewPagerLazyFragment implements
        AMap.OnMarkerClickListener,AMapPoiListView{

    //显示地图需要的变量
    @Bind(R.id.map)
    MapView mMapView;//地图控件
    @Bind(R.id.iv_mylocation)
    ImageView ivMyLocation;
    @Bind(R.id.btn_poi_search)
    Button btnPoiSearch;
    private AMap mAMap;

    private MyLocationHandler myLocationHandler;
    private PoiSearchHandler mPoiSearchHandler;

    private int mCurrentPage = 1;
    //当前定位的city
    private String mCity = "深圳";
    //定位的经度
    private double mLatitude;
    //定位的纬度
    private double mLongitude;

    //搜索范围
    public static final int POI_SEARCH_AREA = 5000;

    private PoiListPopupWindow mPoiLitPopupWindow;
    private List<MapPoiBean> mMapPoiBeanList;

    @Inject
    AMapPoiListPresenter presenter;

    public static MeishiMapFragment newInstance() {

        return new MeishiMapFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_poi;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        //必须要写
        mMapView.onCreate(savedInstanceState);

        if (mAMap == null) {
            //获取地图对象
            mAMap = mMapView.getMap();
            mAMap.setOnMarkerClickListener(this);
        }

        if(myLocationHandler == null) {
            myLocationHandler = new MyLocationHandler(getActivity(),mAMap);
        }

        if(mPoiSearchHandler == null) {
            mPoiSearchHandler = new PoiSearchHandler(getActivity(),mAMap);
        }

        initPopupWindow();

        if(presenter != null && !mHasLoadedOnce) {
            presenter.attachView(this);
        }
    }

    private void initPopupWindow() {
        mPoiLitPopupWindow = new PoiListPopupWindow(getContext(),new ArrayList<MapPoiBean>());

//        mPoiLitPopupWindow.setItemClickListener(this);
        mPoiLitPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();

        //开始定位
        myLocationHandler.startLocation();

        mHasLoadedOnce = true;
    }


    @OnClick({R.id.iv_mylocation,R.id.btn_poi_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mylocation:
                LogUtils.d("start orientation.....");
                myLocationHandler.startLocation();
                break;
            case R.id.btn_poi_search:
//                mPoiSearchHandler.setmCurrentPage(mCurrentPage);
                poiSearch2();
                break;
        }
    }

    private void poiSearch2() {

        if(mLatitude == 0) {
            mLatitude = myLocationHandler.getmLatitude();
        }
        mPoiSearchHandler.setmLatitude(mLatitude);
        if(mLongitude == 0) {
            mLongitude = myLocationHandler.getmLongitude();
        }

        if(presenter != null) {
            presenter.getAmapPoiList(mLatitude,mLongitude,mCurrentPage);
        }
    }

    private void poiSearch1() {
        if(TextUtils.isEmpty(mCity)) {
            mCity = myLocationHandler.getmCity();
        }
        mPoiSearchHandler.setmCity(mCity);
        if(mLatitude == 0) {
            mLatitude = myLocationHandler.getmLatitude();
        }
        mPoiSearchHandler.setmLatitude(mLatitude);
        if(mLongitude == 0) {
            mLongitude = myLocationHandler.getmLongitude();
        }
        mPoiSearchHandler.setmLongitude(mLongitude);
        mPoiSearchHandler.doSearchQuery();
    }

    @Override
    protected void initInjector() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        LogUtils.d("onpause deactive  ");

        myLocationHandler.stopLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mMapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        myLocationHandler.onDestory();
        myLocationHandler = null;
    }

    @Override
    public void renderPoiListData(List<MapPoiBean> datas) {
        this.mMapPoiBeanList = datas;

        LogUtils.d(datas);
        if(mPoiLitPopupWindow != null) {
            View bottomContainer = ((MainActivity) getActivity()).getBottomContainer();
            mPoiLitPopupWindow.showAsDropDown(bottomContainer, 0, -bottomContainer.getHeight());
            mPoiLitPopupWindow.setDatas(datas,0);
        }
        mPoiSearchHandler.renderPoiResultToMap(datas);
    }

    @Override
    public void renderData(List<BaseBean> datas) {

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
    public boolean onMarkerClick(Marker marker) {

        mPoiSearchHandler.onMarkerClick(marker);

        if(mPoiLitPopupWindow != null) {
            View bottomContainer = ((MainActivity) getActivity()).getBottomContainer();
            mPoiLitPopupWindow.showAsDropDown(bottomContainer, 0, -bottomContainer.getHeight());
            mPoiLitPopupWindow.setDatas(mMapPoiBeanList,mPoiSearchHandler.getLastMarkerPosition());
        }

        //return false 则会弹出infowindow
        return true;
    }
}
