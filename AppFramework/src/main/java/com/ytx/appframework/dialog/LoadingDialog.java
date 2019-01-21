package com.ytx.appframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import com.ytx.appframework.R;

/**
 * Created by fangcan on 2018/4/18.
 */

public class LoadingDialog extends Dialog {
    private TextView messageView;
    private String message = "加载中";

    public void setMessage(String message) {
        this.message = message;
    }

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.AFLoadingDialog);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_loading_dialog);
        messageView = findViewById(R.id.tv_message);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        messageView.setText(message);
    }
}
