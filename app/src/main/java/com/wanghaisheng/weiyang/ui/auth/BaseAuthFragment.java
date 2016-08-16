package com.wanghaisheng.weiyang.ui.auth;


import com.wanghaisheng.template_lib.ui.base.BaseFragment;
import com.wanghaisheng.template_lib.widget.mrdialog.MrHUD;
import com.wanghaisheng.weiyang.R;
/**
 * Created by sheng on 2016/7/13.
 */
public abstract class BaseAuthFragment extends BaseFragment {

    private MrHUD dialog;

    public void hideLoading() {
        hideWaitDialog();
    }

    public void showLoading() {
        showWaitDialog("正在提交数据。。。");
    }

    public void showWaitDialog() {
        showWaitDialog(R.string.loading);
    }

    public void showWaitDialog(int resid) {
        showWaitDialog(getString(resid));
    }

    public void showWaitDialog(String message) {
        if(dialog == null) {
            dialog = new MrHUD(getActivity());
        }
        dialog.showLoadingMessage(message,true);
    }

    public void hideWaitDialog() {
        dialog.dismiss();
    }

    public void showInfo(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(getActivity());
        }
        dialog.showInfoMessage(msg);
    }

    public void showInfo(int msgRes) {
        showInfo(getString(msgRes));
    }

    public void showError(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(getActivity());
        }

        dialog.showErrorMessage(msg);
    }

    public void showSuccess(String msg) {
        if(dialog == null) {
            dialog = new MrHUD(getActivity());
        }
        dialog.showSuccessMessage(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null) {
            dialog.dismiss();
        }
    }
}
