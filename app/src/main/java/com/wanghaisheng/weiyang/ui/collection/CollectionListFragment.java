package com.wanghaisheng.weiyang.ui.collection;

import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.DividerItemDecoration;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.ui.base.BaseLazyListFragment;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.presenter.common.CollectionListPresenter;
import com.wanghaisheng.weiyang.presenter.common.CollectionListView;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by sheng on 2016/6/24.
 */
public class CollectionListFragment extends BaseLazyListFragment<BaseBean> implements CollectionListView,ItemDeleteListener {
    private static final String TAG = "CollectionListFragment";
    public static final String ARG_MODULE = "arg_module";
    public static final String MODULE_ARTICLE = "module_articlebean";
    public static final String MODULE_PICTURE = "module_picturebean";
    public static final String MODULE_MEISHI = "module_meishi";
    public static final String MODULE_MOVIE = "module_movie";

    @Inject
    CollectionListPresenter presenter;

    private String module;

    public static CollectionListFragment newInstance(String module) {
        CollectionListFragment fragment = new CollectionListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MODULE,module);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public CommonAdapter<BaseBean> initAdapter() {
        return new ArticleAndPictureAdapter(getActivity(),mDatas,this);

    }

    @Override
    public void onRefreshData() {
        if(null !=presenter) {
            page = 1;
            presenter.loadData(module,page);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(null !=presenter) {
            presenter.loadData(module,page);
        }
    }

    @Override
    public void onReloadClicked() {
        if(null != presenter) {
            page = 1;
            presenter.loadData(module,page);
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
        this.module = bundle.getString(ARG_MODULE);
    }

    @Override
    public void initView(View view,Bundle savedInstanceState) {
        if(!MODULE_MEISHI.equals(module)) {
            myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        }
        rootView = view.findViewById(R.id.content_root);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if(null != presenter) {
            LogUtils.d("first load load data  ");
            presenter.attachView(this);
            showLoading();
        }
    }

    @Override
    public void renderCollectionData(List<BaseBean> datas) {
        // page==0则为第一次加载，page==1 则为刷新，page>1则为加载更多
        LogUtils.d(datas);
        initEmptyLayout();
        if(page == 1) {
            onRefreshComplete();
            LogUtils.d("page  "+page);
            if(!ListUtils.isEmpty(datas)) {
                mDatas.clear();
                isFirstLoad = false;
                page = 2;
                mDatas.addAll(0,datas);
                mAdapter.notifyItemRangeChanged(0,datas.size());
            } else {
                emptyLayout.setNodata();
            }

            return;
        }

        onLoadMoreComplete();
        if (!ListUtils.isEmpty(datas)) {
            page += 1;
            LogUtils.d(datas);
            int lastIndex = mDatas.size();
            mDatas.addAll(lastIndex,datas);
            mAdapter.notifyItemRangeChanged(lastIndex,datas.size());
        }

        if(ListUtils.isEmpty(datas)) {
            setCanLoadMore(false);
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
    public void itemDeleted(int position) {
        presenter.removeCollection(mDatas.get(position));
        mDatas.remove(position);
        mAdapter.notifyItemRangeRemoved(position,1);
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
