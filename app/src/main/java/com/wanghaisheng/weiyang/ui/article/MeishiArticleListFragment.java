package com.wanghaisheng.weiyang.ui.article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.DividerItemDecoration;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.common.CommonListView;
import com.wanghaisheng.template_lib.ui.base.BaseLazyListFragment;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.presenter.common.CollectionAndLikePresenter;
import com.wanghaisheng.weiyang.presenter.common.CollectionView;
import com.wanghaisheng.weiyang.presenter.caipu.MeishiArticleListPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Author: sheng on 2016/8/8 11:21
 * Email: 1392100700@qq.com
 */
public class MeishiArticleListFragment extends BaseLazyListFragment<BaseBean> implements CommonListView,CollectionView {
    private static final String TAG = "CommonListFragment";
    public static final String ARG_CHANNEL_ENTITIES = "arg_channel_entities";

    @Inject
    MeishiArticleListPresenter presenter;
    @Inject
    CollectionAndLikePresenter collectPresenter;

    private ZhuantiChannelEntity channelEntity;//加载的是哪个module

    public static MeishiArticleListFragment newInstance(ZhuantiChannelEntity channelEntity) {
//        LogUtils.d(channelEntity.getChannelEntities());
        MeishiArticleListFragment fragment = new MeishiArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CHANNEL_ENTITIES, channelEntity);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public AppCommonAdapter initAdapter() {
        return new AppCommonAdapter(getActivity(),mDatas);
    }

    @Override
    protected void setupAutoLoadmore() {
        super.setupAutoLoadmore();
        int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();
        //lastVisibleItem >= totalItemCount - 6 表示剩下6个item自动加载，各位自由选择
        if (lastVisibleItem >= totalItemCount - 6) {
            if(!isLoadingMore){
                autoLoadMore();//这里多线程也要手动控制isLoadingMore
            }
        }
    }

    private void autoLoadMore() {
        isLoadingMore = true;
        presenter.loadListData(page);
    }

    @Override
    public void onRefreshData() {
        if(null !=presenter) {
            if(isFirstLoad) {
                page = 1;
            } else {
                page = 0;
            }
            LogUtils.d("onrefresh data  ...");
            presenter.loadListData(page);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(null !=presenter) {
            LogUtils.d("zhuanti list fragment load more data  ");
            presenter.loadListData(page);
            isLoadingMore = true;
        }
    }

    @Override
    public void onReloadClicked() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void initInjector() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        channelEntity = (ZhuantiChannelEntity) getArguments().getSerializable(ARG_CHANNEL_ENTITIES);
    }

    @Override
    public void initView(View view,Bundle savedInstanceState) {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rootView = view.findViewById(R.id.content_root);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if(null != presenter) {
            LogUtils.d("first load load data  ");
            presenter.attachView(this);
            presenter.setChannelEntity(channelEntity);
            collectPresenter.attachView(this);
            showLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
    }

    @Override
    public void renderData(List<BaseBean> datas) {
        // page==0则为第一次加载，page==1 则为刷新，page>1则为加载更多
//        LogUtils.d(datas);
        if(page == 1 || page == 0) {
            onRefreshComplete();
            mHasLoadedOnce = true;
            if(!ListUtils.isEmpty(datas)) {
                mDatas.clear();
                page = 2;
                mDatas.addAll(datas);
                mAdapter.notifyDataSetChanged();
            } else {
                emptyLayout.setRetry("暂无内容，点击屏幕重试");
                swipeToLoadLayout.setVisibility(View.GONE);
            }

            return;
        }
        onLoadMoreComplete();
        if (!ListUtils.isEmpty(datas)) {
            page += 1;
            addDatas(datas);
            isLoadingMore = false;
        }
    }

    private void addDatas(List<BaseBean> datas) {
        List<BaseBean> tempDatas = new ArrayList<>();
        tempDatas.addAll(mDatas);
        int lastIndex = mDatas.size();
        for(BaseBean baseBean : datas) {
            if(!mDatas.contains(baseBean)) {
                mDatas.add(baseBean);
            }
        }
        int insertCount = mDatas.size()-tempDatas.size();
        mAdapter.notifyItemRangeChanged(lastIndex,insertCount);
    }

    @Override
    public void updateCollectionResult(boolean collected) {
        if(collected) {
            ToastUtil.showSnackbar("收藏成功",rootView);
        } else {
            ToastUtil.showSnackbar("取消收藏",rootView);
        }
    }

    @Override
    public void updateCheckResult(boolean collected) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
//        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
        LogUtils.d("onActivityResult");
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(channelEntity.getChannel_title()); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(channelEntity.getChannel_title());
        if(presenter != null) {
            presenter.pause();
        }
    }


}
