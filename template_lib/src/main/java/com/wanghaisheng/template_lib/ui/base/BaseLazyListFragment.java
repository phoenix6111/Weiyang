package com.wanghaisheng.template_lib.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.apkfuns.logutils.LogUtils;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sheng on 2016/4/14.
 */
public abstract class BaseLazyListFragment<T> extends BaseViewPagerLazyFragment {

    protected SwipeToLoadLayout swipeToLoadLayout;
    protected RecyclerView myRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected CommonAdapter<T> mAdapter;

    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息
    public static final String ARG_EMPTYSTATE = "arg_emptystate";

    //recyclerview中的数据
    protected List<T> mDatas = new ArrayList<>();

    protected boolean firstLoad = true;//是否是第一次加载，如果是第一次加载才调用initdata中的
        // presenter.firstLoadData()方法加载数据，否则不调用，避免过多的网络请求
    protected int page = 1;

    //是否是滚动状态
    protected boolean isScrolling = false;
    //是否是第一次加载，如果是第一次加载则执行initData中的数据
    protected boolean isFirstLoad = true;
    protected boolean isLoadingMore = false;

    protected View rootView;

    //初始化UI相关的属性
    @Override
    public void beforeInitView(View view,Bundle savedInstanceState) {
        parentView = view;
        myRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mLayoutManager = getRecyclerViewLayoutManager(view);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = initAdapter();
        myRecyclerView.setAdapter(mAdapter);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData();
            }
        });
        myRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                //当停止滑动时加载图片
                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }

                setupAutoLoadmore();

                super.onScrollStateChanged(recyclerView,newState);
            }

        });

        swipeToLoadLayout.setRefreshCompleteDelayDuration(200);
        swipeToLoadLayout.setLoadMoreCompleteDelayDuration(200);
        swipeToLoadLayout.setDefaultToRefreshingScrollingDuration(200);
        swipeToLoadLayout.setDefaultToLoadingMoreScrollingDuration(200);

    }

    protected void setupAutoLoadmore() {}

    //设置recyclerview的布局管理器，如果子类要改变recyclerview的布局，则修改此方法的返回值
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new LinearLayoutManager(view.getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list_layout;
    }

    protected void initEmptyLayout() {
        if(emptyLayout == null) {
            ViewStub viewStub = (ViewStub) parentView.findViewById(R.id.viewstub);
            emptyLayout = (EmptyLayout) viewStub.inflate();
            emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReloadClicked();
                }
            });
        }
    }

    public void hideLoading() {
//        initEmptyLayout();
        swipeToLoadLayout.setVisibility(View.VISIBLE);
        swipeToLoadLayout.setRefreshing(false);
    }

    public void showLoading() {
//        initEmptyLayout();
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public void loadError(int errorType, AppException ex) {
        LogUtils.d(ex);
        /*initEmptyLayout();
        switch (errorType) {
            case IView.LOAD_TYPE_FIRSTLOAD:
                if (ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                    emptyLayout.setNetworkError();
                } else {
                    emptyLayout.setRetry(ErrorMessageFactory.getMessage(getContext(), ex.getCode()));
                }

                swipeToLoadLayout.setVisibility(View.GONE);
                break;
            case IView.LOAD_TYPE_REFRESH:
                onRefreshComplete();
                String errMsg = null;
                if (ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                    errMsg = "网络连接错误";
                } else {
                    errMsg = "数据刷新失败";
                }
                toastUtil.showSnackbar(errMsg, rootView);
                break;
            case IView.LOAD_TYPE_LOADMORE:
                onLoadMoreComplete();
//                setCanLoadMore(false);
                String errMsg2 = null;
                if (ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                    errMsg2 = "网络连接错误";
                } else {
                    errMsg2 = "没有更多内容";
                }
                toastUtil.showSnackbar(errMsg2, rootView);
                break;
            case IView.LOAD_TYPE_COLLECT:
                String errMsg3 = null;
                if (ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                    errMsg3 = "网络连接错误,取消收藏失败";
                } else {
                    errMsg3 = "收藏失败";
                }
                toastUtil.showSnackbar(errMsg3, rootView);
                break;
            case IView.LOAD_TYPE_UNCOLLECT:
                String errMsg4 = null;
                if (ex.getCode() == AppException.ERROR_TYPE_NETWORK) {
                    errMsg4 = "网络连接错误,取消收藏失败";
                } else {
                    errMsg4 = "取消收藏失败";
                }
                toastUtil.showSnackbar(errMsg4, rootView);
                break;
            default:
                toastUtil.showSnackbar("发生错误", rootView);
        }*/
    }

    //初始化适配器
    public abstract CommonAdapter<T> initAdapter();

    //当数据刷新完成时调用
    public void onRefreshComplete() {
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    public void onLoadMoreComplete() {
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    public abstract void onRefreshData();

    public abstract void onLoadMoreData();

    public void setCanRefresh(boolean canRefresh) {
        swipeToLoadLayout.setRefreshEnabled(canRefresh);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        swipeToLoadLayout.setLoadMoreEnabled(canLoadMore);
    }

    public abstract void onReloadClicked();


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(emptyLayout != null) {
            outState.putInt(ARG_EMPTYSTATE, emptyLayout.getErrorState());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if(emptyLayout != null) {
                mStoreEmptyState = savedInstanceState.getInt(ARG_EMPTYSTATE, 4);
                emptyLayout.setErrorType(mStoreEmptyState);
            }
        }
    }
}
