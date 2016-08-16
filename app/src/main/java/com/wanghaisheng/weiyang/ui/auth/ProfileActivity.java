package com.wanghaisheng.weiyang.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.component.imagefileselector.ImageCropper;
import com.wanghaisheng.template_lib.component.imagefileselector.ImageFileSelector;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.User;
import com.wanghaisheng.weiyang.datasource.repository.UserStorage;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.auth.ProfilePresenter;
import com.wanghaisheng.weiyang.presenter.auth.ProfileView;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by sheng on 2016/5/24.
 */
public class ProfileActivity extends BaseActivity implements ProfileView{
    private static final String TAG = "ProfileActivity";

    public static final String TYPE_UPDATE_NICKNAME = "type_update_nickname";
    public static final String TYPE_UPDATE_GENDER = "type_update_gender";
    public static final String TYPE_UPDATE_AVATAR = "type_update_avatar";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_nickname)
    TextView tvNickName;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_tel)
    TextView tvTel;
    /*@Bind(R.id.tv_wechat)
    TextView tvWechat;*/
    @Bind(R.id.sd_avatar)
    MySimpleDraweeView draweeView;

    private User mUser;

    @Inject
    UserStorage userStorage;

    @Inject
    ProfilePresenter presenter;

    private ImageFileSelector mImageFileSelector;
    private ImageCropper mImageCropper;
    private File mCurrentSelectFile;

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
        return R.layout.act_auth_profile;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        tvTitle.setText(R.string.act_profile_toolbar_title);

    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
        }

        //初始化图片选择器
        mImageFileSelector = new ImageFileSelector(this);
        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(final String file) {
                if (!TextUtils.isEmpty(file)) {
                    mCurrentSelectFile = new File(file);

                    //选择了文件之后立即裁剪
                    if (mCurrentSelectFile.exists()) {
                        mImageCropper.setOutPut(300, 300);
                        mImageCropper.setOutPutAspect(1, 1);
                        mImageCropper.cropImage(mCurrentSelectFile);
                    }
                } else {
                    ToastUtil.showToast(ProfileActivity.this,"select image file loadError");
                }
            }

            @Override
            public void onError() {
                ToastUtil.showToast(ProfileActivity.this,"select image file loadError");
            }
        });

        //初始化图片裁剪器
        mImageCropper = new ImageCropper(this);
        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                mCurrentSelectFile = null;

                if (result == ImageCropper.CropperResult.success) {
                    Uri avatarUri = Uri.fromFile(outFile);
                    draweeView.setRoundDraweeViewUri(avatarUri);

                    presenter.updateAvatar(avatarUri);
                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {
                    ToastUtil.showToast(ProfileActivity.this,"input file loadError");
                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {
                    ToastUtil.showCenterToast(ProfileActivity.this,"output file loadError");
                }
            }
        });
    }

    @OnClick({R.id.cv_touxian,R.id.cv_nicnname,R.id.cv_gender,R.id.cv_logout,R.id.btn_update_passwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_touxian:
                handlerTouxian();
                break;
            case R.id.cv_nicnname:
                handlerNickname();
                break;
            case R.id.cv_gender:
                handlerGender();
                break;
            case R.id.cv_logout:
                logout();
                break;
            case R.id.btn_update_passwd:
                Navigator.openUpdatewordView(ProfileActivity.this);
                break;
        }
    }


    /**
     * 修改头像
     */
    private void handlerTouxian() {
        new MaterialDialog.Builder(this)
                .items(R.array.profile_touxian_dialog_list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                mImageFileSelector.takePhoto(ProfileActivity.this);
                                break;
                            case 1:
                                mImageFileSelector.selectImage(ProfileActivity.this);
                                break;
                        }
                    }
                })
                .show();

    }

    /**
     * 修改昵称
     */
    private void handlerNickname() {
        new MaterialDialog.Builder(this)
                .title(R.string.act_profile_dialog_nickname_title)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 16)
                .positiveText(R.string.common_dialog_positive_text)
                .negativeText(R.string.common_dialog_negtive_text)
                .input(mUser.getNick_name(), mUser.getNick_name(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        presenter.updateNickname(input.toString());
                    }
                }).show();
    }

    /**
     * 修改性别
     */
    private void handlerGender() {
        int index = getGenderIndex(mUser.getGender());
        new MaterialDialog.Builder(this)
                .title(R.string.act_profile_dialog_gender_title)
                .items(R.array.profile_gender_dialog_list)
                .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        LogUtils.d(text.toString());
                        presenter.updateGender(text.toString());
                        return true;
                    }
                })
                .positiveText(R.string.common_dialog_positive_text)
                .negativeText(R.string.common_dialog_negtive_text)
                .show();
    }

    private int getGenderIndex(String gender) {
        String[] genders = getResources().getStringArray(R.array.profile_gender_dialog_list);
        LogUtils.d(genders);
        if(genders[0].equals(gender)) {
            return 0;
        }

        return 1;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mUser = userStorage.getLoginUser();

        LogUtils.d(mUser);

        tvNickName.setText(mUser.getNick_name());
        tvGender.setText(mUser.getGender());
        tvTel.setText(mUser.getTel());
//        tvWechat.setText(mUser.getWechat_name());
        if(TextUtils.isEmpty(mUser.getAvatar())) {
            draweeView.setRoundDraweeViewResId(R.drawable.user_avatar);
        } else {
            draweeView.setRoundDraweeViewUri(Uri.parse(mUser.getAvatar()));
        }

//        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this);          //统计时长
    }

    /**
     * 退出账号
     */
    private void logout() {
        userStorage.logout();
        Navigator.openMainActivity(ProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);
        mImageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
        mImageCropper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
        mImageCropper.onRestoreInstanceState(savedInstanceState);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
    }

    @Override
    public void hideLoading() {
        hideWaitDialog();
    }

    @Override
    public void showLoading() {
        showWaitDialog();
    }

    @Override
    public void loadError(int loadType, AppException e) {
        String errMsg = "";
        switch (loadType) {
            case ProfileView.UPDATE_TYPE_TYPE_UPDATE_AVATAR:
                errMsg = "头像修改失败，";
                if(TextUtils.isEmpty(mUser.getAvatar())) {
                    draweeView.setRoundDraweeViewResId(R.drawable.user_avatar);
                } else {
                    draweeView.setRoundDraweeViewUri(Uri.parse(mUser.getAvatar()));
                }
                break;
            case ProfileView.UPDATE_TYPE_TYPE_UPDATE_GENDER:
                errMsg = "性别信息修改失败，";
                tvGender.setText(mUser.getGender());
                break;
            case ProfileView.UPDATE_TYPE_TYPE_UPDATE_NICKNAME:
                errMsg = "昵称修改失败，";
                tvNickName.setText(mUser.getNick_name());
                break;
        }
        ToastUtil.showCenterToast(ProfileActivity.this,errMsg+ e.getDisplayMessage());

    }

    @Override
    public void updateSuccess(int type, User user) {
        if(ProfileView.UPDATE_TYPE_TYPE_UPDATE_NICKNAME == type) {
            updateNicknameResult(user);
        } else if(ProfileView.UPDATE_TYPE_TYPE_UPDATE_GENDER == type) {
            updateGenderResult(user);
        } else if(ProfileView.UPDATE_TYPE_TYPE_UPDATE_AVATAR == type) {
            updateAvatarResult(user);
        }
    }

    public void updateNicknameResult(User user) {
        showSuccess("昵称更新成功");
        this.mUser = user;
        tvNickName.setText(mUser.getNick_name());

    }

    public void updateGenderResult(User user) {
        showSuccess("用户性别修改成功");
        this.mUser = user;
        tvGender.setText(mUser.getGender());

    }

    public void updateAvatarResult( User user) {
        showSuccess("头像修改成功");
        this.mUser = user;
        draweeView.setRoundDraweeViewUri(Uri.parse(mUser.getAvatar()));
    }

}
