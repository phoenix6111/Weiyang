package com.wanghaisheng.weiyang.presenter.common;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.common.Constants;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.database.MeishiBeanDao;
import com.wanghaisheng.weiyang.ui.collection.CollectionListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by sheng on 2016/6/24.
 */
public class CollectionListPresenter extends BaseLoadablePresenter<CollectionListView> {

    @Inject
    Lazy<MeishiBeanDao> articleDaoLazy;

    @Inject
    public CollectionListPresenter() {}

    /**
     * 加载收藏的数据
     * @param module
     * @param page
     */
    public void loadData(final String module, final int page) {
        LogUtils.d("module  collection list presenter load data");

        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<BaseBean>>() {
            @Override
            public void call(Subscriber<? super List<BaseBean>> subscriber) {
                subscriber.onNext(getCollectDataByPage(module,page));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<List<BaseBean>>() {
                    @Override
                    protected void onError(AppException ex) {
                        CollectionListView iView = getView();
                        if(iView != null) {
                            if(page == 1) {
                                hideViewLoading();
                                iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                            } else {
                                iView.loadError(IView.LOAD_TYPE_LOADMORE,ex);
                            }
                        }
                    }

                    @Override
                    public void onNext(List<BaseBean> baseBeen) {
                        CollectionListView iView = getView();
                        if(iView != null) {
                            if(page == 1) {
                                hideViewLoading();
                            }
                            LogUtils.d(baseBeen);
                            iView.renderCollectionData(baseBeen);
                        }
                    }
                });

        compositeSubscription.add(subscription);

    }

    /**
     * 根据不同的模块，加载不同的数据
     */
    private List<BaseBean> getCollectDataByPage(String module, int page) {

        if(CollectionListFragment.MODULE_ARTICLE.equals(module)) {
            final int offset = Constants.ARTICLE_COLLECTION_PAGE_LIMIT * (page-1);
            List<MeishiBean> datas = articleDaoLazy.get().queryBuilder()
                    .where(MeishiBeanDao.Properties.ModuleName.notEq(ModuleConstants.MODULE_IDENTITY_DOUGUO)
                            ,MeishiBeanDao.Properties.IsCollected.eq(true))
                    .offset(offset)
                    .limit(Constants.ARTICLE_COLLECTION_PAGE_LIMIT).list();
            List<BaseBean> baseBeen = new ArrayList<>();
            for (MeishiBean articleBean : datas) {
                if(!TextUtils.isEmpty(articleBean.getImageUrls())) {
                    String[] splitStr = articleBean.getImageUrls().split("\\|");
                    List<String> result = new ArrayList<>(Arrays.asList(splitStr));
                    articleBean.setImageUrlList(result);
                }

                baseBeen.add(articleBean);
            }

            return baseBeen;
        }

        return null;
    }

    /**
     * 删除收藏的数据
     * @param baseBean
     */
    public void removeCollection(BaseBean baseBean) {
        Observable<Boolean> observable = null;
        if(baseBean instanceof MeishiBean) {
            final MeishiBean articleBean = (MeishiBean) baseBean;
            observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    articleDaoLazy.get().delete(articleBean);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            });
        }
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<Boolean>() {
                    @Override
                    protected void onError(AppException ex) {
                        CollectionListView iView = getView();
                        if(null != iView) {
                            iView.loadError(IView.LOAD_TYPE_COLLECT,ex);
                        }
                    }

                    @Override
                    public void onNext(Boolean result) {

                    }
                });

        compositeSubscription.add(subscription);
    }
}
