package com.wanghaisheng.weiyang.presenter.amap;

import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseListLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Author: sheng on 2016/8/15 20:05
 * Email: 1392100700@qq.com
 */
public class AMapPoiListPresenter extends BaseListLoadablePresenter<AMapPoiListView> {

    @Inject
    AMapRepository aMapRepository;

    @Inject
    public AMapPoiListPresenter() {}

    public void getAmapPoiList(double latitude,double longitude,int page) {
        Subscription subscription = aMapRepository.subscribeMapPoiBeanList(latitude,longitude,page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<List<MapPoiBean>>() {
                    @Override
                    protected void onError(AppException ex) {
                        AMapPoiListView iView = getView();
                        if(iView != null) {
                            iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(List<MapPoiBean> mapPoiBeen) {
                        AMapPoiListView iView = getView();
                        if(iView != null) {
                            iView.renderPoiListData(mapPoiBeen);
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }
}
