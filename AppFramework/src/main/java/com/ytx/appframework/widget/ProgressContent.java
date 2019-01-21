package com.ytx.appframework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ytx.appframework.R;


/**
 * Created by zhiguo.jiang on 17/6/21.
 */

public class ProgressContent extends FrameLayout {
    private View contentView;
    private View progressView;
    private View errorView;
    private View emptyView;

    private ImageView emptyImageView;
    private ImageView errorImageView;

    private int emptyViewId;
    private int errorViewId;
    private int progressViewId;

    private int emptyImgRes;
    private int errorImgRes;

    public void setEmptyImgRes(@DrawableRes int emptyImgRes) {
        this.emptyImgRes = emptyImgRes;
        if (emptyImageView != null) {
            emptyImageView.setImageResource(emptyImgRes);
        }
    }

    public void setErrorImgRes(@DrawableRes int errorImgRes) {
        this.errorImgRes = errorImgRes;
        if (errorImageView != null) {
            errorImageView.setImageResource(errorImgRes);
        }
    }

    private OnProgressItemClickListener progressItemClickListener;

    public void setProgressItemClickListener(
            OnProgressItemClickListener progressItemClickListener) {
        this.progressItemClickListener = progressItemClickListener;
    }

    public ProgressContent(@NonNull Context context) {
        super(context);
    }

    public ProgressContent(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressContent(@NonNull Context context, @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressContent);
        emptyViewId =
                typedArray.getResourceId(R.styleable.ProgressContent_empty_view, R.layout.widget_progress_empty_view);
        errorViewId =
                typedArray.getResourceId(R.styleable.ProgressContent_error_view, R.layout.widget_progress_error_view);
        progressViewId =
                typedArray.getResourceId(R.styleable.ProgressContent_progress_view, R.layout.widget_progress_loading_view);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(0) == null || getChildCount() > 1) {
            throw new IllegalArgumentException("should only have one child");
        }
        contentView = getChildAt(0);
        initProgressView(progressViewId);
        showContent();
    }

    private void initProgressView(int progressViewId) {
        if (progressView == null) {
            progressView = LayoutInflater.from(getContext()).inflate(progressViewId, this, false);
            addView(progressView);
        }
    }

    private void initErrorView(int errorViewId) {
        if (errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(errorViewId, this, false);
            addView(errorView, 1);

            View view = errorView.findViewById(R.id.error_view);
            if (view != null && view instanceof ImageView) {
                errorImageView = (ImageView) view;
                if (errorImgRes > 0) {
                    errorImageView.setImageResource(errorImgRes);
                }
            }
            initErrorClickListener(view);
        }
    }

    private void initEmptyView(int emptyViewId) {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(emptyViewId, this, false);
            addView(emptyView, 1);

            View view = emptyView.findViewById(R.id.empty_view);
            if (view != null && view instanceof ImageView) {
                emptyImageView = (ImageView) view;
                if (emptyImgRes > 0) {
                    emptyImageView.setImageResource(emptyImgRes);
                }
            }
        }
    }

    private void initErrorClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (progressItemClickListener != null) {
                        progressItemClickListener.onErrorClick();
                    }
                }
            });
        }
    }

    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }

    public void showError() {
        if (errorView == null) {
            initErrorView(errorViewId);
        }
        errorView.setVisibility(View.VISIBLE);
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }

    public void showEmpty() {
        if (emptyView == null) {
            initEmptyView(emptyViewId);
        }
        emptyView.setVisibility(View.VISIBLE);
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (contentView != null) {
            contentView.setVisibility(GONE);
        }
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }

    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    public void justShowProgress() {
        progressView.setVisibility(View.VISIBLE);
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
    }

    public static interface OnProgressItemClickListener {
        public void onErrorClick();
    }
}
