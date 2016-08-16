package com.wanghaisheng.weiyang.ui.my;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.ui.base.BaseFragment;
import com.wanghaisheng.weiyang.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: sheng on 2016/8/11 16:36
 * Email: 1392100700@qq.com
 */
public class MeFragment extends BaseFragment {

    @Bind(R.id.rl_tab)
    RelativeLayout rlTab;
    @Bind(R.id.me_iv_profile)
    MySimpleDraweeView meIvProfile;
    @Bind(R.id.me_tv_username)
    TextView meTvUsername;
    @Bind(R.id.me_btn_login)
    Button meBtnLogin;
    @Bind(R.id.me_cv_profile)
    CardView meCvProfile;
    @Bind(R.id.icon_setup)
    ImageView iconSetup;
    @Bind(R.id.setup_card)
    CardView setupCard;
    @Bind(R.id.icon_feedback)
    ImageView iconFeedback;
    @Bind(R.id.feedback)
    CardView feedback;
    @Bind(R.id.icon_about)
    ImageView iconAbout;
    @Bind(R.id.about_card)
    CardView aboutCard;
    @Bind(R.id.icon_collection)
    ImageView iconCollection;
    @Bind(R.id.cv_collection)
    CardView cvCollection;
    @Bind(R.id.icon_poi)
    ImageView iconPoi;
    @Bind(R.id.cv_poi)
    CardView cvPoi;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_me_me;
    }

    @Override
    protected void initInjector() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_cv_profile, R.id.setup_card, R.id.feedback, R.id.about_card, R.id.cv_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_cv_profile:
                break;
            case R.id.setup_card:
                break;
            case R.id.feedback:
                break;
            case R.id.about_card:
                break;
            case R.id.cv_collection:
                break;

        }
    }
}
