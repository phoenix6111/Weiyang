package com.wanghaisheng.weiyang.presenter.amap;

import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;
import com.wanghaisheng.weiyang.datasource.repository.amap.AMapRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Author: sheng on 2016/8/16 21:06
 * Email: 1392100700@qq.com
 */
public class AMapPoiDetailPresenter extends BaseLoadablePresenter<AMapPoiDetailView> {

    @Inject
    AMapRepository aMapRepository;

    @Inject
    public AMapPoiDetailPresenter() {}

    public void getMapPoiDetail(MapPoiBean mapPoiBean) {

        Subscription subscription = aMapRepository.subscribeMapPoiBeanDetail(mapPoiBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<MapPoiDetailBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        AMapPoiDetailView iView = getView();

                        if(iView != null) {
                            iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(MapPoiDetailBean detailBean) {
                        AMapPoiDetailView iView = getView();

                        if(iView != null) {
                            iView.renderAMapPoiDetail(detailBean);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}
