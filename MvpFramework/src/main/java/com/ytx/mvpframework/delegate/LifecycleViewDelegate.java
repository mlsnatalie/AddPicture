package com.ytx.mvpframework.delegate;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.ytx.mvpframework.presenter.LifecycleViewDelegatePresenter;

public class LifecycleViewDelegate<T extends LifecycleViewDelegatePresenter> extends ViewDelegate<T> implements LifecycleObserver {

    private Lifecycle lifecycle = null;
    private boolean isDestroy = false;

    public final void bindLifecycle(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner.getLifecycle() != lifecycle) {
            if (lifecycle != null) {
                lifecycle.removeObserver(this);
            }
            lifecycleOwner.getLifecycle().addObserver(this);
            this.lifecycle = lifecycleOwner.getLifecycle();
            if (presenter != null) {
                presenter.bindLifecycle(lifecycleOwner);
            }
        }
    }

    public final void unbindLifecycle() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
            this.lifecycle = null;
        }
        if (presenter != null) {
            presenter.unbindLifecycle();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        isDestroy = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        unbind();
        destroyView();
        isDestroy = true;
        lifecycle = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny(LifecycleOwner owner) {

    }

    public final boolean isCreated() {
        if (lifecycle != null) {
            return lifecycle.getCurrentState().isAtLeast(Lifecycle.State.CREATED);
        } else {
            return false;
        }
    }

    public final boolean isResume() {
        if (lifecycle != null) {
            return lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED);
        } else {
            return false;
        }
    }

    public final boolean isStart() {
        if (lifecycle != null) {
            return lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        } else {
            return false;
        }
    }

    public final boolean isDestroy() {
        return isDestroy;
    }
}
