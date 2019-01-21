package com.ytx.mvpframework.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;

import com.ytx.mvpframework.view.IView;

public class FragmentPresenter<V extends IView> extends LifecyclePresenter<V> {
    private boolean isViewCreated = false;

    public FragmentPresenter(V view) {
        super(view);
        if (view instanceof Fragment) {
            bindFragment((Fragment) view);
        }
    }

    public final void bindFragment(Fragment view) {
        bindLifecycle(view);
    }

    public final void unbindFragment() {
        unbindLifecycle();
    }

    public void onViewCreated(LifecycleOwner owner) {
        isViewCreated = true;
    }

    public void onDestroyView(LifecycleOwner owner) {
        isViewCreated = false;
    }

    public final boolean isViewCreated() {
        return isViewCreated;
    }
}
