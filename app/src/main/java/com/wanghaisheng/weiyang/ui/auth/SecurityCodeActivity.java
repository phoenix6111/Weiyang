package com.wanghaisheng.weiyang.ui.auth;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.ui.base.BaseActivity;
import com.wanghaisheng.template_lib.utils.RegexUtils;
import com.wanghaisheng.template_lib.utils.ToastUtil;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.injector.component.DaggerActivityComponent;
import com.wanghaisheng.weiyang.injector.module.ActivityModule;

import java.util.concurrent.TimeUnit;

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
 * Created by sheng on 2016/7/8.
 */
public class SecurityCodeActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_security_info)
    TextView tvSecutityInfo;
    @Bind(R.id.et_tel)
    TextView etTel;
    @Bind(R.id.btn_time)
    Button btnTimer;
    @Bind(R.id.btn_complete)
    Button btnComplete;

    private Subscription timer = null;

    @Override
    public void getDatas(Bundle savedInstanceState) {

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
        return R.layout.act_auth_security_code;
    }

    @Override
    public void initView() {
        initToolbar(toolbar);
        tvTitle.setText(getString(R.string.auth_secutirycode_toolbar_title));
        String templateStr = getString(R.string.act_profile_security_info);
        String securityCode = CIAService.getSecurityCode();
        String formatStr = String.format(templateStr,securityCode);
        tvSecutityInfo.setText(formatStr);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                if(timer!= null && !timer.isUnsubscribed()) {
                    timer.unsubscribe();
                }
                onBackPressed();
            }
        });

        etTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(RegexUtils.match("^\\d{4}$",s.toString())) {
                    btnComplete.setEnabled(true);
                } else {
                    btnComplete.setEnabled(false);
                }
            }

            @Override
           public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initData() {
        initTimer();
    }

    @OnClick({R.id.btn_complete})
    public void onClick(View view) {
        String code = etTel.getText().toString();

        CIAService.verifySecurityCode(code, new VerificationListener() {
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
                LogUtils.d(msg);
                LogUtils.d(CIAService.getSecurityCode());
                switch (status) {
                    case CIAService.VERIFICATION_SUCCESS: // 验证成功
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        ToastUtil.showCenterToast(SecurityCodeActivity.this,"验证成功");
                        setResult(RESULT_OK);
                        finish();

                        break;
                    case CIAService.SECURITY_CODE_EXPIRED:  // 验证码失效，需要重新验证
                        ToastUtil.showCenterToast(SecurityCodeActivity.this,"验证码失效，请重新验证");
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        finish();
                        break;

                    case CIAService.SECURITY_CODE_WRONG:
                        if(null != timer) {
                            timer.unsubscribe();
                        }
                        ToastUtil.showCenterToast(SecurityCodeActivity.this,"验证码验证失败，需要重新验证");
                        break;
                    case CIAService.SECURITY_CODE_EXPIRED_INPUT_OVERRUN:    // 验证码输入错误次数过多(3次)，需要重新验证
                        ToastUtil.showCenterToast(SecurityCodeActivity.this,"验证码输入错误超过3次，请重新验证");
                        finish();
                        break;
                }
            }
        });
    }

    //初始化倒计时
    private void initTimer() {
        final int countTime = 60;

        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
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
                        btnTimer.setEnabled(false);
                        btnTimer.setText(str);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CIAService.cancelVerification();
    }
}
