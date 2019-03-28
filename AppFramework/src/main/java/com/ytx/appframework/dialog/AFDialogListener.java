package com.ytx.appframework.dialog;

/**
 * Created by linxi on 2018/4/23.
 */

public abstract class AFDialogListener {
    public abstract void onConfirm();

    public void onCancel() {
        // do nothing
    }

    public void onDismiss() {
        // do nothing
    }
}
