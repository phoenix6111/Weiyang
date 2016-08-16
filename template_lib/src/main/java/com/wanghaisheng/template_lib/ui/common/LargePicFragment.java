package com.wanghaisheng.template_lib.ui.common;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.component.fresco.photodraweeview.OnPhotoTapListener;
import com.wanghaisheng.template_lib.component.fresco.photodraweeview.PhotoDraweeView;
import com.wanghaisheng.template_lib.ui.base.BaseFragment;

/**
 * Created by sheng on 2016/5/8.
 */
public class LargePicFragment extends BaseFragment {
    public static final String ARG_INDEX = "arg_index";
    public static final String ARG_URL = "arg_url";

    private String url;

    PhotoDraweeView mPhotoDraweeView;

    public static LargePicFragment newInstance(int index, String url) {
        LargePicFragment fragment = new LargePicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_INDEX,index);
        bundle.putString(ARG_URL,url);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_large_pic;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        this.url = getArguments().getString(ARG_URL);
    }

    @Override
    public void initView(View view,Bundle savedInstanceState) {
        this.mPhotoDraweeView = (PhotoDraweeView) view.findViewById(R.id.image);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(url));
        controller.setAutoPlayAnimations(true);
        controller.setOldController(mPhotoDraweeView.getController());
        // You need setControllerListener
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null) {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mPhotoDraweeView.setController(controller.build());
        mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                LargePicActivity activity = (LargePicActivity) getActivity();
                activity.showOrHideToolbar();
            }
        });

    }

    public View getSharedElement() {
        return mPhotoDraweeView;
    }

}
