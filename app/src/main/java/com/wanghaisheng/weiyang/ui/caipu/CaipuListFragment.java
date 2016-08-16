package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.wanghaisheng.template_lib.component.flowlayout.TagAdapter;
import com.wanghaisheng.template_lib.component.flowlayout.TagFlowLayout;
import com.wanghaisheng.template_lib.ui.base.BaseFragment;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;


/**
 * Created by sheng on 2016/7/4.
 */
public class CaipuListFragment extends BaseFragment {
    private static final String TAG = "CaipuListFragment";
    public static final String ARG_PRIMARY_POS = "arg_primary_pos";

    @Bind(R.id.flowlayout)
    TagFlowLayout tagFlowLayout;
    TagAdapter<String> tagAdapter;
    List<String> mDatas = new ArrayList<>();

    private int primaryPos;

    public static CaipuListFragment newInstance(int position) {
        CaipuListFragment fragment = new CaipuListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PRIMARY_POS,position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_meishi_caipu_secondcate;
    }

    @Override
    protected void initInjector() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        primaryPos = bundle.getInt(ARG_PRIMARY_POS);
    }


    @Override
    public void initView(View view,Bundle savedInstanceState) {
        tagFlowLayout.setAdapter(initAdapter());
        tagFlowLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_CENTER);
    }

    @Override
    public void initData() {


    }

    private TagAdapter<String> initAdapter() {
        String selectedCate = getActivity().getResources().getStringArray(R.array.meishi_cate_array)[primaryPos];
        String[] selectedCates = selectedCate.split(",");
        mDatas.addAll(Arrays.asList(selectedCates));
        tagAdapter = new TagAdapter<String>(mDatas) {
            @Override
            protected View getView(ViewGroup parent, int position, String cate) {
                TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_meishi_caipu_second_cate_textview,
                        parent, false);
                tv.setText(cate);
                return tv;
            }

            @Override
            protected void onSelect(ViewGroup parent, View view, int position) {
                LogUtils.d("meishi second caipu on select  cate  "+mDatas.get(position));
                Navigator.openMeishiCaipuDetailActivity(getActivity(),mDatas.get(position));
            }

            @Override
            protected void onUnSelect(ViewGroup parent, View view, int position) {
                LogUtils.d("meishi second caipu on unselect  cate  "+mDatas.get(position));
//                navigator.openMeishiCaipuDetailActivity(getActivity(),mDatas.get(position));
            }
        };

        return tagAdapter;
    }



}
