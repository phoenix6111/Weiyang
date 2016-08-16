package com.wanghaisheng.weiyang.ui.caipu;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.component.fresco.LoadingProgressDrawable;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.presenter.common.CommonListView;
import com.wanghaisheng.template_lib.presenter.common.CommonSearchView;
import com.wanghaisheng.template_lib.ui.base.BaseNormalListFragment;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.beans.ChannelEntity;
import com.wanghaisheng.weiyang.datasource.beans.ZhuantiChannelEntity;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.caipu.MeishiArticleListPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by sheng on 2016/7/5.
 */
public class MeishiSearchResultListFragment extends BaseNormalListFragment implements CommonListView,CommonSearchView {
    public static final String ARG_CHANNEL_ENTITIES = "arg_channel_entities";

    @Inject
    MeishiArticleListPresenter presenter;

    private String searchTag;//搜索字段
    private ZhuantiChannelEntity channelEntity;//加载的是哪个module

    public static MeishiSearchResultListFragment newInstance() {
        return new MeishiSearchResultListFragment();
    }

    @Override
    public CommonAdapter<BaseBean> initAdapter() {
        return new CommonAdapter<BaseBean>(getActivity(), R.layout.item_meishi_meishilist,mDatas) {
            @Override
            public void convert(ViewHolder holder, BaseBean baseBean, int position) {
                final MeishiBean content = (MeishiBean) baseBean;
                holder.setText(R.id.tv_meishi_title, content.getTitle());
                MySimpleDraweeView image = holder.getView(R.id.mdv_meishi_img);
                image.setPlaceholderDrawable(new ColorDrawable(ContextCompat.getColor(mContext,R.color.white)))
                        .setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                        .setDraweeViewUrl(content.getImageUrlList().get(0));

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigator.openArticleBeanDetailActivity(mContext,content);
                    }
                });
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(null !=presenter) {
            page = 0;
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_searchable_list;
    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
//            presenter.setChannelEntity(channelEntity);
        }
    }

    @Override
    public void renderData(List<BaseBean> datas) {

        if(page == 0||page == 1) {
            onRefreshComplete();
            if(!ListUtils.isEmpty(datas)) {
                mDatas.clear();
                isFirstLoad = false;
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

    @Override
    public void searchResultByTag(String tag) {
        this.searchTag = tag;
        LogUtils.d("search tag  "+tag);
        page = 1;
        presenter.setChannelEntity(buildChannelEntity(this.searchTag));
        presenter.loadListData(page);
        showLoading();
    }

    private ZhuantiChannelEntity buildChannelEntity(String caipu) {
        ZhuantiChannelEntity channelEntity = new ZhuantiChannelEntity();
        channelEntity.setChannel_tag("zhuanti_module_meishi_search");
        channelEntity.setChannel_title("美食搜索");

        List<ChannelEntity> channelEntities = new ArrayList<>();
        ChannelEntity entity = new ChannelEntity();
        entity.setChannelIdentity("douguo|search|"+caipu);
        entity.setChannelTitle(caipu);
        entity.setModuleTitle("美食菜谱");
        channelEntities.add(entity);
        channelEntity.setChannelEntities(channelEntities);

        return channelEntity;
    }


    /**
     * 清空搜索结果
     */
    @Override
    public void clearSearchResult() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
    }
}
