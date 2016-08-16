package com.wanghaisheng.weiyang.presenter.common;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.datasource.repository.common.SearchTagRepository;
import com.wanghaisheng.template_lib.presenter.AppSubscriber;
import com.wanghaisheng.template_lib.presenter.base.BasePresenter;
import com.wanghaisheng.template_lib.presenter.base.IView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by sheng on 2016/6/12.
 */
public class SearchHistoryPresenter extends BasePresenter<SearchHistoryView> {
    @Inject
    SearchTagRepository repository;

    @Inject
    public SearchHistoryPresenter(){}

    /**
     * 根据key加载所有搜索tag
     */
    public void loadSearchHistoryTags(String moduleKey) {
        Subscription subscription = repository.loadSearchHistoryTag(moduleKey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSubscriber<List<String>>() {
                    @Override
                    protected void onError(AppException ex) {
                        SearchHistoryView iView = getView();
                        if(null != iView) {
                            iView.loadError(IView.LOAD_TYPE_FIRSTLOAD,ex);
                        }
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        SearchHistoryView iView = getView();
                        if(null != iView) {
                            iView.renderSearchHistoryResult(strings);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void saveSearchHistoryTags(String key, List<String> tags) {
        Subscription subscription = repository.saveSearchHistoryTag(key,tags)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d("搜索记录保存失败。。");
                    }
                });
        compositeSubscription.add(subscription);
    }

}
