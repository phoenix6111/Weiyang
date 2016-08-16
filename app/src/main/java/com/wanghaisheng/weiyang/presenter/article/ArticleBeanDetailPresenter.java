package com.wanghaisheng.weiyang.presenter.article;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BaseLoadablePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;
import com.wanghaisheng.weiyang.datasource.repository.DataHandlerFactory;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sheng on 2016/6/19.
 */
public class ArticleBeanDetailPresenter extends BaseLoadablePresenter<ArticleBeanDetailView> {

    @Inject
    DataHandlerFactory dataHandlerFactory;

    @Inject
    SohuRepository sohuRepository;

    @Inject
    public ArticleBeanDetailPresenter(){}

    /**
     * 获取详情html
     * @param baseBean
     */
    public void loadContentHtml(BaseBean baseBean) {
        showViewLoading();

        Subscription subscription = dataHandlerFactory.getArticleBeanDetail(baseBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<String>() {
                    @Override
                    protected void onError(AppException ex) {
                        hideViewLoading();
                        ArticleBeanDetailView detailView = getView();
                        if(null != detailView) {
                            detailView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(String content) {
//                        LogUtils.d("content  "+content);
                        hideViewLoading();
                        ArticleBeanDetailView detailView = getView();
//                        LogUtils.d(detailView);
                        if(null != detailView) {
                            LogUtils.d("render webview..............");
                            detailView.renderWebview(content);
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }


    public void loadSohuMoreContent(String id) {
        Subscription subscription = sohuRepository.loadMoreContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<String>() {
                    @Override
                    protected void onError(AppException ex) {
                        LogUtils.d(ex);
                    }

                    @Override
                    public void onNext(String str) {
                        ArticleBeanDetailView iView = getView();
                        if(iView != null) {
                            iView.sohuLoadMoreContent(str);
                        }
                    }
                });
    }
}
