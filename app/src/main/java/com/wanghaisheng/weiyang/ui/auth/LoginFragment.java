package com.wanghaisheng.weiyang.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.utils.TDevice;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.auth.AuthPresenter;
import com.wanghaisheng.weiyang.presenter.auth.AuthView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by sheng on 2016/6/24.
 */
public class LoginFragment extends BaseAuthFragment implements AuthView {
    private static final String TAG = "LoginFragment";

    @Bind(R.id.et_tel)
    EditText etTel;
    @Bind(R.id.et_passswd)
    EditText etPassswd;

    @Inject
    AuthPresenter presenter;

    private String userTel;//用户电话号码
    private String passwd;//用户密码

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void initInjector() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frgt_auth_login;
    }

    @Override
    public void getSavedBundle(Bundle bundle) {

    }

    @Override
    public void initView(View view,Bundle savedInstanceState) {
        etTel.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    @Override
    public void initData() {
        if(presenter != null) {
            presenter.attachView(this);
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_passwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                handlerLogin();
                break;
            case R.id.tv_forget_passwd:
                Navigator.openRegetPasswordView(getActivity());
        }
    }

    public void handlerLogin() {
        //如果输入错误，则不继续执行
        if(inputError()) {
            return;
        }

        userTel = etTel.getText().toString().trim();
        passwd = etPassswd.getText().toString().trim();

        presenter.login(userTel,passwd);
    }

    /**
     * 判断是否能提交注册请求
     * @return
     */
    private boolean inputError() {
        if (!TDevice.hasInternet()) {
            ToastUtil.showToast(getActivity(),R.string.common_no_network);
            return true;
        }
        if (etTel.length() == 0) {
            etTel.setError("请输入手机号");
            etTel.requestFocus();
            return true;
        }

        if (etPassswd.length() == 0) {
            etPassswd.setError("请输入密码");
            etPassswd.requestFocus();
            return true;
        }

        return false;
    }

    public void success() {
        LogUtils.d("登陆成功");
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Navigator.openMainActivity(getActivity());
                        getActivity().finish();
                    }
                });
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }


    @Override
    public void handlerResult(boolean success) {
        showSuccess("登陆成功");
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Navigator.openMainActivity(getActivity());
                    }
                });
    }

    @Override
    public void loadError(int loadType, AppException e) {
        switch (e.getCode()) {
            case AppException.ERROR_TYPE_VALIDATE:
                showError("登陆失败，用户名或密码错误");
                return;
            case AppException.ERROR_TYPE_NOT_FOUND:
                showError("登陆失败，找不到用户信息");
                return;
        }

        showError("登陆失败， "+e.getDisplayMessage());
    }
}
