package com.wanghaisheng.weiyang.ui.collection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wanghaisheng.template_lib.ui.base.BaseTopNagigationFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sheng on 2016/4/20.
 */
public class CollectionTopNavigationFragment extends BaseTopNagigationFragment {
    public List<String> collectionTitles = new ArrayList<>();

    public static CollectionTopNavigationFragment newInstance() {
        return new CollectionTopNavigationFragment();
    }

    @Override
    protected FragmentStatePagerAdapter initPagerAdapter() {
        collectionTitles.add("文章");
        collectionTitles.add("图片");
        collectionTitles.add("电影");
        collectionTitles.add("美食菜谱");

        return new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return collectionTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return collectionTitles.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                String module = null;
                switch (position) {
                    case 0:
                        module = CollectionListFragment.MODULE_ARTICLE;
                        break;
                    case 1:
                        module = CollectionListFragment.MODULE_PICTURE;
                        break;
                    case 2:
                        module = CollectionListFragment.MODULE_MOVIE;
                        break;
                    case 3:
                        module = CollectionListFragment.MODULE_MEISHI;
                        break;
                }

                return CollectionListFragment.newInstance(module);
            }
        };
    }

}
