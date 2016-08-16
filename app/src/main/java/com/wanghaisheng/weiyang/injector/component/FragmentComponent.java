package com.wanghaisheng.weiyang.injector.component;

import android.app.Activity;

import com.wanghaisheng.template_lib.ui.base.BaseViewPagerLazyFragment;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.injector.scopes.PerFragment;
import com.wanghaisheng.weiyang.ui.article.BaseMeishiArticleFragment;
import com.wanghaisheng.weiyang.ui.article.MeishiArticleListFragment;
import com.wanghaisheng.weiyang.ui.auth.LoginFragment;
import com.wanghaisheng.weiyang.ui.auth.RegisterFragment;
import com.wanghaisheng.weiyang.ui.caipu.BaseMeishiCaipuFragment;
import com.wanghaisheng.weiyang.ui.caipu.CaipuListFragment;
import com.wanghaisheng.weiyang.ui.caipu.MeishiListFragment;
import com.wanghaisheng.weiyang.ui.caipu.MeishiNoLazyListFragment;
import com.wanghaisheng.weiyang.ui.caipu.MeishiSearchResultListFragment;
import com.wanghaisheng.weiyang.ui.collection.CollectionListFragment;
import com.wanghaisheng.weiyang.ui.poi.MeishiMapFragment;

import dagger.Component;


/**
 * Created by sheng on 2016/4/13.
 */

@PerFragment
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {
    Activity getActiviy();

//    void inject(BaseFragment fragment);

    void inject(BaseViewPagerLazyFragment lazyFragment);

    void inject(BaseMeishiCaipuFragment baseMeishiFragment);

    void inject(MeishiSearchResultListFragment meishiSearchResultListFragment);

    void inject(MeishiNoLazyListFragment meishiNoLazyListFragment);

    void inject(MeishiListFragment meishiListFragment);

    void inject(CaipuListFragment caipuListFragment);

    void inject(BaseMeishiArticleFragment baseMeishiArticleFragment);

    void inject(MeishiArticleListFragment meishiArticleListFragment);

    void inject(MeishiMapFragment meishiPoiFragment);

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);

    void inject(CollectionListFragment collectionListFragment);


}
