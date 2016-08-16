package com.wanghaisheng.template_lib.presenter.common;

import com.wanghaisheng.template_lib.presenter.base.IView;

/**
 * Created by sheng on 2016/6/12.
 */
public interface SavePictureView extends IView {

    /**
     * 当图片保存完成时
     * @param imgPath 图片的保存路径
     */
    void onImageSaved(boolean imgSaved, String imgPath) ;

}
