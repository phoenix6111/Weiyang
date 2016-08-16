package com.wanghaisheng.weiyang.presenter.common;


import com.wanghaisheng.template_lib.presenter.base.IView;

/**
 * Created by sheng on 2016/6/12.
 */
public interface CollectionView extends IView {
    void updateCollectionResult(boolean collected);

    void updateCheckResult(boolean collected);
}
