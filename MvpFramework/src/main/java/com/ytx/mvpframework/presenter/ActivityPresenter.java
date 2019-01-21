package com.ytx.mvpframework.presenter;

import android.support.v4.app.FragmentActivity;

import com.ytx.mvpframework.view.IView;

public class ActivityPresenter<V extends IView> extends LifecyclePresenter<V>  {

    public ActivityPresenter(V view) {
        super(view);
        if (view instanceof FragmentActivity) {
            bindActivity((FragmentActivity) view);
        }
    }

    public final void bindActivity(FragmentActivity activity) {
        bindLifecycle(activity);
    }

    public final void unbindActivity() {
        unbindLifecycle();
    }
}
