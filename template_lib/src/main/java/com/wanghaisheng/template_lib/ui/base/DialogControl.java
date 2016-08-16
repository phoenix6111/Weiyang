package com.wanghaisheng.template_lib.ui.base;

public interface DialogControl {

	void hideWaitDialog();

	void showWaitDialog();

	void showWaitDialog(int resid);

	void showWaitDialog(String text);

	void showInfo(String info);

	void showError(String info);

	void showSuccess(String info);
}