package com.wanghaisheng.template_lib.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wanghaisheng.template_lib.AppManager;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.utils.ResourceHelper;
import com.wanghaisheng.template_lib.utils.StatusBarUtil;
import com.wanghaisheng.template_lib.widget.mrdialog.MrHUD;

import butterknife.ButterKnife;

/**
 * Created by sheng on 2016/4/13.
 */
public abstract class BaseActivity extends AppCompatActivity implements DialogControl {

    private static final String TAG = "BaseActivity";

    protected LayoutInflater mInflater;
    private boolean _isVisible = true;
    private MrHUD dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();

        //在setContentView之前执行的方法
        onBeforeSetContentLayout();

        if(0 != getLayoutId()) {
            setContentView(getLayoutId());
        }
        mInflater = getLayoutInflater();

        ButterKnife.bind(this);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setTranslucentStatus(isApplyStatusBarTranslucency());
                setStatusBarColor(isApplyStatusBarColor());
            }
        });

        getDatas(savedInstanceState);

        //initdata之前执行的方法
        onBeforeInitData();

        initData();
        initView();

        AppManager.addActivity(this);
    }

    protected void initView(){}

    protected void initData(){}

    protected void onBeforeInitData(){}

    protected void onBeforeSetContentLayout(){}

    protected abstract void initInjector();

    public void getDatas(Bundle savedInstanceState){}

    /**
     * is applyStatusBarTranslucency
     *
     * @return
     */
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    protected boolean isApplyStatusBarColor() {
        return true;
    }


    public void setStatusBarColor(boolean on) {
        if (on) {
            StatusBarUtil.setColor(this, ResourceHelper.getThemeColor(this), 0);
        }
    }

    protected void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.tabbar_back_filter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.finishActivity(this);

        if(dialog != null) {
            dialog.dismiss();
        }
    }

    //get layout file id
    protected abstract int getLayoutId();

    protected <T extends View> T findById(int id) {
        return (T) findViewById(id);
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    @Override
    public void showWaitDialog() {
        showWaitDialog(R.string.loading);
    }

    @Override
    public void showWaitDialog(int resid) {
        showWaitDialog(getString(resid));
    }

    @Override
    public void showWaitDialog(String message) {
        if(dialog == null) {
            dialog = new MrHUD(BaseActivity.this);
        }
        dialog.showLoadingMessage(message,true);
    }

    @Override
    public void hideWaitDialog() {
        dialog.dismiss();
    }

    public void showInfo(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(BaseActivity.this);
        }
        dialog.showInfoMessage(msg);
    }

    public void showInfo(int msgRes) {
        showInfo(getString(msgRes));
    }

    public void showError(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(BaseActivity.this);
        }

        dialog.showErrorMessage(msg);
    }

    public void showSuccess(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(BaseActivity.this);
        }
        dialog.showSuccessMessage(msg);
    }
}
