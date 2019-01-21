package com.ytx.mvpframework.delegate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytx.mvpframework.presenter.ViewDelegatePresenter;

public class ViewDelegate<T extends ViewDelegatePresenter> {
    private static String TAG = "ViewDelegate";
    private ViewGroup rootView;
    private View view;
    private boolean isBinded;
    private boolean isViewCreated;
    protected T presenter;

    public ViewDelegate() {
        presenter = createPresenter();
    }

    protected T createPresenter() {
        return null;
    }
    
    public final Context getContext() {
        if (rootView != null) {
            return rootView.getContext();
        } else if (view != null) {
            return view.getContext();
        } else {
            return null;
        }
    }

    public final void bindToView(ViewGroup parent) {
        if (rootView == parent || parent == null) {
            return ;
        }
        if (rootView != null && view != null) {
            unbind();
        }
        boolean isCreateFlag = false;
        if (this.view == null) {
            View view = onCreateView(LayoutInflater.from(parent.getContext()), parent);
            if (view == null) {
                Log.i(TAG, "warning: onCreateView return null");
                return ;
            }
            this.view = view;
            isCreateFlag = true;
        }
        this.rootView = parent;
        this.rootView.addView(view);
        isViewCreated = true;
        isBinded = true;
        if (isCreateFlag) {
            onViewCreated(view);
            if (presenter != null) {
                presenter.onViewCreated();
            }
        }
        onBind(view);
        if (presenter != null) {
            presenter.onBind();
        }
    }


    public final void unbind() {
        if (isBinded) {
            isBinded = false;
            try {
                onUnBind();
                if (presenter != null) {
                    presenter.onUnBind();
                }
            } finally {
                rootView.removeView(view);
                rootView = null;
            }
        }
    }

    public final View getView() {
        return view;
    }

    public final ViewGroup getRootView() {
        return rootView;
    }
    
    public final boolean isBinded() {
        return isBinded;
    }
    
    public final boolean isViewCreated() {
        return isViewCreated;
    }

    /**
     * 销毁view, 必需先调unbind方法解除绑定
     */
    public final void destroyView() {
        if (isBinded()) {
            Log.i(TAG, "WARNING: destroyView failed, must be call unbind()");
            return ;
        }
        if (isViewCreated) {
            isViewCreated = false;
            try {
                onDestroyView();
                if (presenter != null) {
                    presenter.onDestroyView();
                }
            } finally {
                view = null;
            }
        } else {
            Log.i(TAG, "WARNING: destroyView failed, isViewCreated is false");
        }
    }

    /**
     * 每次调 bindToView，如果view is null，则会 call this method 
     */
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent) {
        return null;
    }

    /**
     * 每次 onCreateView 后，则会 call this method
     */
    protected void onViewCreated(View view) {

    }
    
    protected void onDestroyView() {
        
    }

    /**
     * 每次成功 bindToView 时的回调
     */
    protected void onBind(View view) {
        
    }

    /**
     * 每次成功 unbind 时的回调
     */
    protected void onUnBind() {
        
    }
}
