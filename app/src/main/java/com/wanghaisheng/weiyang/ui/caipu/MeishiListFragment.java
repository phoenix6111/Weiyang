package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.common.CommonListView;
import com.wanghaisheng.template_lib.ui.base.BaseLazyListFragment;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.presenter.caipu.MeishiArticleListPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sheng on 2016/7/4.
 */
public class MeishiListFragment extends BaseLazyListFragment<BaseBean> implements CommonListView {
    private static final String TAG = "MeishiListFragment";
    public static final String ARG_CHANNEL_ENTITIES = "arg_channel_entities";

    @Inject
    MeishiArticleListPresenter presenter;

    private ZhuantiChannelEntity channelEntity;//加载的是哪个module

    public static MeishiListFragment newInstance(ZhuantiChannelEntity channelEntity) {
        LogUtils.d(channelEntity);
        MeishiListFragment fragment = new MeishiListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CHANNEL_ENTITIES, channelEntity);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public CommonAdapter<BaseBean> initAdapter() {
        return new MeishiAdapter(getActivity(),mDatas);
    }

    @Override
    public void onRefreshData() {
        if(null !=presenter) {
            if(isFirstLoad) {
                page = 1;
            } else {
                page = 0;
            }
            presenter.loadListData(page);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(null !=presenter) {
            LogUtils.d("zhuanti list fragment load more data  ");
            presenter.loadListData(page);
        }
    }

    @Override
    public void onReloadClicked() {
        if(null != presenter) {
            page = 1;
            presenter.loadListData(page);
        }
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
        rootView = view.findViewById(R.id.content_root);
        LogUtils.d("meishi list fragment init view ...");
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_searchable_list;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        LogUtils.d("meishi list fragment init data..");
        if(null != presenter) {
            presenter.attachView(this);
            presenter.setChannelEntity(channelEntity);
            LogUtils.d("first load load data  ");
            showLoading();
        }

    }

    @Override
    public void renderData(List<BaseBean> datas) {
        if(page == 0||page == 1) {
            onRefreshComplete();
            if(!ListUtils.isEmpty(datas)) {
                mDatas.clear();
                mHasLoadedOnce = true;
                page = 2;
                mDatas.addAll(datas);
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        onLoadMoreComplete();
        if (!ListUtils.isEmpty(datas)) {
            page += 1;
            int lastIndex = mDatas.size();
            mDatas.addAll(lastIndex,datas);
            mAdapter.notifyItemRangeChanged(lastIndex,datas.size());
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

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
    }

}
