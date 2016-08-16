package com.wanghaisheng.weiyang.ui.caipu;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.DividerItemDecoration;
import com.wanghaisheng.template_lib.presenter.common.CommonSearchView;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.InputMethodUtils;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.common.Constants;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.presenter.common.SearchHistoryPresenter;
import com.wanghaisheng.weiyang.presenter.common.SearchHistoryView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;


/**
 * Created by sheng on 2016/6/2.
 */
public class MeishiSearchActivity extends BaseActivity implements SearchHistoryView {
    private static final String TAG = "MeishiSearchActivity";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.iv_action_search)
    ImageView ivActionSearch;
    @Bind(R.id.rv_search_history_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.searchhistory_content)
    LinearLayout searchHistoryContent;
    @Bind(R.id.content)
    FrameLayout searchListContent;
    @Bind(R.id.btn_clear_history)
    TextView clearHistory;

    CommonAdapter<String> mAdapter;
    List<String> mSearchTags = new ArrayList<>();

    private CommonSearchView meishiSearchView;
    private boolean isShowed = false;//该变量是键盘显示的标志，第一次打开时为false，如果打开了详情，再回到列表，则不打开键盘
    @Inject
    SearchHistoryPresenter presenter;

    public void setMeishiSearchView(CommonSearchView meishiSearchView) {
        this.meishiSearchView = meishiSearchView;
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((AppContext)getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_act_search;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);

        /*searchView.setVisibility(View.VISIBLE);
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);
        ImageView btnGo = (ImageView) searchView.findViewById(R.id.search_go_btn);
        btnGo.setImageResource(R.drawable.tabbar_search_api_mtrl_alpha_filter);
        final EditText mEdit = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        mEdit.setBackgroundResource(R.drawable.bg_searchview);

        if (Build.VERSION.SDK_INT >= 14) {
            searchView.onActionViewExpanded();
        }

        ImageView closeImg = (ImageView) searchView.findViewById(R.id.search_close_btn);
        /*//*********坑:3.2以上的不能fire  oncloseListener，只能这样获取到closeImg监听事件了
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("search btn close");
                mEdit.setText("");

                //当清空搜索关键词的时候，就该清空搜索结果，显示搜索历史记录
                meishiSearchView.clearSearchResult();
                searchListContent.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                searchHistoryContent.setVisibility(View.VISIBLE);
            }
        });

        // show keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtils.d("meishi search activity query  "+query);
                //隐藏输入法
                InputMethodUtils.hideSoftInput(MeishiSearchActivity.this);
                String queryStr = query.trim();

                //当点击搜索按钮时则隐藏搜索记录，显示搜索结果
                searchHistoryContent.setVisibility(View.GONE);
                searchListContent.setVisibility(View.VISIBLE);

                meishiSearchView.searchResultByTag(query.trim());

                //如果以前的搜索记录中不包含该搜索tag，则将其加入
                if(!mSearchTags.contains(queryStr)) {
                    mSearchTags.add(queryStr);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });*/

        ivActionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hideSoftInput(MeishiSearchActivity.this);
                String queryStr = etSearch.getText().toString().trim();
                if(TextUtils.isEmpty(queryStr)) {
                    etSearch.setError("请输入搜索字段");
                    return;
                }

                handlerSearchAction(queryStr);
            }
        });

        //监听键盘 搜索事件
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if(EditorInfo.IME_ACTION_SEND == actionId || (event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    InputMethodUtils.hideSoftInput(MeishiSearchActivity.this);

                    String queryStr = textView.getText().toString().trim();
                    handlerSearchAction(queryStr);

                    return true;
                }

                return false;
            }
        });
    }

    private void handlerSearchAction(String queryStr) {
        //当点击搜索按钮时则隐藏搜索记录，显示搜索结果
        searchHistoryContent.setVisibility(View.GONE);
        searchListContent.setVisibility(View.VISIBLE);

        meishiSearchView.searchResultByTag(queryStr);

        //如果以前的搜索记录中不包含该搜索tag，则将其加入
        if(!mSearchTags.contains(queryStr)) {
            mSearchTags.add(queryStr);
        }

    }

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MeishiSearchActivity.this, DividerItemDecoration.VERTICAL_LIST));
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("clear search history .....");
                mSearchTags.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter = new CommonAdapter<String>(MeishiSearchActivity.this, R.layout.item_act_common_searchhistory_list, mSearchTags) {
            @Override
            public void convert(final ViewHolder holder, final String tag, final int position) {
                holder.setText(R.id.tv_search_tag,tag);
                holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        removeData(position);//***********坑一：不能直接调用position删除：参考 http://blog.csdn.net/wangkai0681080/article/details/50082825
                        mSearchTags.remove(holder.getAdapterPosition());
                        notifyItemRangeRemoved(holder.getAdapterPosition(),1);

                    }
                });

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.d("meishi search tag item clicked  tag  "+tag);
                        searchHistoryContent.setVisibility(View.GONE);
                        searchListContent.setVisibility(View.VISIBLE);

                        meishiSearchView.searchResultByTag(tag);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        MeishiSearchResultListFragment fragment = MeishiSearchResultListFragment.newInstance();
        setMeishiSearchView(fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();

        if(null != presenter) {
            presenter.attachView(this);
            presenter.loadSearchHistoryTags(Constants.SEARCH_HISTORY_KEY_MEISHI);
        }
    }

    /**
     * 在onStop的时候将搜索记录加入缓存
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("ondestroy mdatas");
        LogUtils.d(mSearchTags);

        if(null != presenter) {
            LogUtils.d("meishi search save search tags");
            LogUtils.d(mSearchTags);
            presenter.saveSearchHistoryTags(Constants.SEARCH_HISTORY_KEY_MEISHI,mSearchTags);
        }
        isShowed = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        etSearch.setFocusable(true);
        InputMethodUtils.showSoftInput(etSearch);
    }

    @Override
    public void renderSearchHistoryResult(List<String> datas) {
        if(!ListUtils.isEmpty(datas)) {
            clearHistory.setVisibility(View.VISIBLE);

            mSearchTags.clear();
            mSearchTags.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void loadError(int loadType, AppException ex) {

    }

}
