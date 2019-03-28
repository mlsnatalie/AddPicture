package com.ytx.appframework.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytx.appframework.R;


/**
 * Created by linxi on 2018/4/23.
 */

public class SimpleDialog extends Dialog implements IAlertDialog, IConfirmDialog {

    ViewGroup bottomView;
    TextView titleView;
    TextView contentView;
    TextView leftActionView;
    TextView rightActionView;

    private View line2;
    private View line3;

    private String titleText;
    private String contentText;
    private String rightText;
    private String leftText;

    private Integer leftTextColor;
    private Integer rightTextColor;

    private AFDialogListener listener;

    public void setListener(AFDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @Override
    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public void setLeftTextColor(int color) {
        this.leftTextColor = color;
    }

    public void setRightTextColor(int color) {
        rightTextColor = color;
    }

    public SimpleDialog(@NonNull Context context) {
        this(context, R.style.AFSimpleDialog);
    }

    public SimpleDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        rightText = context.getResources().getString(R.string.af_dialog_confirm);
        leftText = context.getResources().getString(R.string.af_dialog_cancel);
    }

    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof ContextThemeWrapper) {
            return (Activity) ((ContextThemeWrapper) context).getBaseContext();
        } else {
            return (Activity) context;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initView();
    }

    protected int getLayoutResource() {
        return R.layout.af_dialog_simple;
    }

    protected void initView() {
        titleView = findViewById(R.id.title);
        contentView = findViewById(R.id.content);
        leftActionView = findViewById(R.id.left);
        rightActionView = findViewById(R.id.right);
        bottomView = findViewById(R.id.ll_bottom_container);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);

        leftActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftAction();
            }
        });

        rightActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightAction();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.onDismiss();
                    listener = null;
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        updateTitleView();
        updateContentView();
        updateLeftActionView();
        updateRightActionView();
        if (line3 != null) {
            if (leftActionView.getVisibility() == View.VISIBLE && rightActionView.getVisibility() == View.VISIBLE) {
                line3.setVisibility(View.VISIBLE);
            } else {
                line3.setVisibility(View.GONE);
            }
        }
        if (bottomView != null) {
            bottomView.setVisibility(TextUtils.isEmpty(leftText) && TextUtils.isEmpty(rightText) ? View.GONE : View.VISIBLE);
        }
    }

    protected void onLeftAction() {
        if (listener != null) {
            listener.onCancel();
        }
        dismiss();
    }

    protected void onRightAction() {
        if (listener != null) {
            listener.onConfirm();
        }
        dismiss();
    }

    protected void updateTitleView() {
        if (titleView != null) {
            if (TextUtils.isEmpty(titleText)) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(titleText);
            }
        }
    }

    protected void updateContentView() {
        if (contentText != null) {
            if (TextUtils.isEmpty(contentText)) {
                contentView.setVisibility(View.GONE);
            } else {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(contentText);
            }
        }
    }

    public TextView getTitleView() {
        return titleView;
    }

    protected void updateLeftActionView() {
        if (leftActionView != null) {
            if (TextUtils.isEmpty(leftText)) {
                leftActionView.setVisibility(View.GONE);
            } else {
                leftActionView.setVisibility(View.VISIBLE);
                leftActionView.setText(leftText);

                updateLeftActionViewColor();
            }
        }
    }

    protected void updateRightActionView() {
        if (rightActionView != null) {
            if (TextUtils.isEmpty(rightText)) {
                rightActionView.setVisibility(View.GONE);
            } else {
                rightActionView.setVisibility(View.VISIBLE);
                rightActionView.setText(rightText);

                updateRightActionViewColor();
            }
        }
    }

    private void updateLeftActionViewColor() {
        if (leftTextColor != null && leftActionView != null) {
            leftActionView.setTextColor(leftTextColor);
        }
    }

    private void updateRightActionViewColor() {
        if (rightTextColor != null && rightActionView != null) {
            rightActionView.setTextColor(rightTextColor);
        }
    }

    public static void alert(Activity activity, String title, String message, AFDialogListener listener) {
        SimpleDialog simpleDialog = new SimpleDialog(activity);
        simpleDialog.setTitleText(title);
        simpleDialog.setContentText(message);
        simpleDialog.setLeftText("");
        simpleDialog.setListener(listener);
        simpleDialog.show();
    }

    public static void confirm(Activity activity, String title, String message, AFDialogListener listener) {
        SimpleDialog simpleDialog = new SimpleDialog(activity);
        simpleDialog.setTitleText(title);
        simpleDialog.setContentText(message);
        simpleDialog.setListener(listener);
        simpleDialog.show();
    }
}
