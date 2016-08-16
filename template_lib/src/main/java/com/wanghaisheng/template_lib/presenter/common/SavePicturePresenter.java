package com.wanghaisheng.template_lib.presenter.common;

import com.wanghaisheng.template_lib.presenter.base.BasePresenter;

/**
 * Created by sheng on 2016/6/12.
 * 保存图片 presenter
 */
public class SavePicturePresenter extends BasePresenter<SavePictureView> {

    public SavePicturePresenter(){}

    /**
     * 保存图片进本地
     * @param imgUrl
     * @param name
     */
    public void saveLargePic(final String imgUrl, final String name) {

        /*fileHelper.saveImagesAndGetPathObservable(imgUrl, Constants.IMAGE_SAVE_FOLDER,name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        SavePictureView iView = getView();
                        if(null != iView) {
                            iView.onImageSaved(true,uri.getPath());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        SavePictureView iView = getView();
                        if(null != iView) {
                            iView.onImageSaved(false,"图片保存失败");
                        }
                    }
                });*/

    }

}
