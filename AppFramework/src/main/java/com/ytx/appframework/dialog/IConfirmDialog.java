package com.ytx.appframework.dialog;

public interface IConfirmDialog {

    void setTitleText(String title);

    void setContentText(String content);

    void show();

    void dismiss();

    void setListener(AFDialogListener listener);
}
