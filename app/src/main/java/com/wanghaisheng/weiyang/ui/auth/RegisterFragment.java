package com.wanghaisheng.weiyang.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.appexception.AppException;
import com.wanghaisheng.template_lib.utils.RegexUtils;
import com.wanghaisheng.template_lib.utils.TDevice;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerFragmentComponent;
import com.wanghaisheng.weiyang.injector.module.FragmentModule;
import com.wanghaisheng.weiyang.navigator.Navigator;
import com.wanghaisheng.weiyang.presenter.auth.AuthPresenter;
import com.wanghaisheng.weiyang.presenter.auth.RegisterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.ciaapp.sdk.CIAService;
import cn.ciaapp.sdk.VerificationListener;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by sheng on 2016/6/24.
 */
public class RegisterFragment extends BaseAuthFragment implements RegisterView {
    private static final String TAG = "RegisterFragment";
    public static final int REQUEST_SECURITY_CODE = 0x001;

    @Bind(R.id.et_tel)
    EditText etTel;
    @Bind(R.id.btn_validate)
    Button btnValidate;
    @Bind(R.id.et_passswd)
    EditText etPassswd;
    @Bind(R.id.btn_register)
    Button btnReg;

    @Inject
    AuthPresenter presenter;


    private String userTel;//用户电话号码
    private String passwd;//用户密码
    private boolean telValidated = false;//手机号是否验证成功
    private Subscription timer = null;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
        return R.layout.frgt_auth_register;
    }

    @Override
    public void getSavedBundle(Bundle bundle) {

    }

    @Override
    public void initView(View view,Bundle savedInstanceState) {
        etTel.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        etTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnValidate.setText(getString(R.string.act_auth_btn_validate_text));
                String text = s.toString().trim();
                if (RegexUtils.isMobileNO(text)) {
                    btnValidate.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //让电话号码获取焦点
        etTel.requestFocus();
    }

    @OnClick({ R.id.btn_register, R.id.btn_validate})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                handlerRegister();
                break;
            case R.id.btn_validate:
                LogUtils.d("handler log validate");
                ToastUtil.showToast(getActivity(),"开始验证");
                LogUtils.d("handler validate");
                handlerValideteTel();
                break;

        }
    }

    public void handlerRegister() {
        if(!telValidated) {
            ToastUtil.showCenterToast(getActivity(),"电话号码未验证，请先验证电话号码");
            return;
        }

        //如果输入错误，则不继续执行
        if (inputError()) {
            return;
        }

        userTel = etTel.getText().toString().trim();
        passwd = etPassswd.getText().toString().trim();

        //根据参数决定是注册还是修改密码
        presenter.register(userTel, passwd);
    }

    /**
     * 判断是否能提交注册请求
     *
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

        passwd = etPassswd.getText().toString().trim();
        if(!RegexUtils.validPasswd(passwd)) {
            etPassswd.setError("密码由大小字字母和数字组成：6到16位");
            etPassswd.requestFocus();
            return true;
        }

        return false;
    }

    /**
     * 验证手机号
     */
    private void handlerValideteTel() {
        LogUtils.d("handlervalidate tel   ");
//        showWaitDialog("正在检测中....");
        String tel = etTel.getText().toString();
        btnValidate.setText("正在发送验证码...");
        //调用CIA验证
        CIAService.startVerification(tel, new VerificationListener() {
            @Override
            public void onStateChange(int status, String msg, String transId) {

                /**
                 * status 是返回的状态码，CIAService包含了一些常量
                 * @see CIAService.VERIFICATION_SUCCESS 验证成功
                 * @see CIAService.VERIFICATION_FAIL 验证失败，请查看 msg 参数描述，例如手机号码格式错误，手机号格式一般需要开发者先校验
                 * @see CIAService.SECURITY_CODE_MODE   验证码模式
                 *      验证码模式：需要提示用户输入验证码，调用
                 *      @see CIAService.getSecurityCode()    获取当前的验证码，格式类似05311234****，需要提示用户****部分是输入的验证码内容
                 * @see CIAService.REQUEST_EXCEPTION    发生异常，msg 是异常描述，例如没有网络连接，网络连接状况一般需要开发者先判断
                 *
                 * 其他情况，status不在上述常量中，是服务器返回的错误，查看 msg 描述，例如 appId 和 authKey 错误。
                 */

                switch (status) {
                    case CIAService.REQUEST_WAIT_CODE://请求成功，等待验证码送达
                        initTimer();
                        break;
                    case CIAService.VERIFICATION_SUCCESS: // 验证成功
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        ToastUtil.showCenterToast(getActivity(),"验证成功");
                        btnValidate.setText("电话号码验证成功");
                        telValidated = true;
                        break;
                    case CIAService.SECURITY_CODE_MODE: // 验证码模式
                        // 进入输入验证码的页面，并提示用户输入验证码
                        //startActivity(new Intent(getApplicationContext(), SecurityCodeActivity.class));
                        ToastUtil.showCenterToast(getActivity(),"自动验证失败，需要手动输入验证码进行验证，将跳转到验证页面");
                        if(null != timer) {
                            timer.unsubscribe();
                            timer = null;
                        }
                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        btnValidate.setText(getString(R.string.act_auth_btn_validate_text));
                                        Intent securityIntent = new Intent(getActivity(),SecurityCodeActivity.class);
                                        startActivityForResult(securityIntent,REQUEST_SECURITY_CODE);
                                    }
                                });
                        break;
                    case CIAService.VERIFICATION_FAIL:
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        ToastUtil.showCenterToast(getActivity(),"验证失败：" + msg);
                        break;
                    case CIAService.REQUEST_EXCEPTION:
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        ToastUtil.showCenterToast(getActivity(),"请求异常：" + msg);
                        break;
                    default:
                        // 服务器返回的错误
                        ToastUtil.showToast(getActivity(),msg);
                }
            }
        });
    }

    //初始化倒计时
    private void initTimer() {
        final int countTime = 60;

        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("开始计时");
                    }
                }).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.d("integer   "+integer);

                        String str = String.format("%d s", integer);
                        btnValidate.setEnabled(false);
                        btnValidate.setText(str);
                    }
                });

    }


    @Override
    public void initData() {
        if(presenter != null) {
            presenter.attachView(this);
        }
    }


    @Override
    public void handlerResult(boolean success) {
        if(success) {
            showSuccess("注册成功成功！");
            Observable.timer(100, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Navigator.openMainActivity(getActivity());
                            getActivity().finish();
                        }
                    });
        }
    }

    @Override
    public void loadError(int loadType, AppException e) {
        LogUtils.d("error code  "+e.getCode()+"  error msg "+e.getDisplayMessage());
        switch (e.getCode()) {
            case AppException.ERROR_TYPE_UNIQUE:
                showError("注册失败，电话号码已注册 ");
                return;
            case AppException.ERROR_TYPE_VALIDATE:
                showError("注册失败，用户名或密码错误");
                return;
        }

        showError("注册失败， "+e.getDisplayMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CIAService.cancelVerification();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SECURITY_CODE && resultCode == Activity.RESULT_OK) {
            telValidated = true;
            btnValidate.setText("电话号码验证成功");
        } else {
            telValidated = false;
            btnValidate.setText(getString(R.string.act_auth_btn_validate_text));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String tel = etTel.getText().toString();
        if (RegexUtils.isMobileNO(tel)) {
            btnValidate.setEnabled(true);
        }
//        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
    }


}
