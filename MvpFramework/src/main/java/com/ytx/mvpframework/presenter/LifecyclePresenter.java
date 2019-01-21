package com.ytx.mvpframework.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.ytx.mvpframework.view.IView;

public class LifecyclePresenter<V extends IView> extends BasePresenter<V> implements LifecycleObserver {
    private Lifecycle lifecycle = null;
    private boolean isDestroy = false;

    public LifecyclePresenter(V view) {
        super(view);
    }

    public final void bindLifecycle(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner.getLifecycle() != lifecycle) {
            if (lifecycle != null) {
                lifecycle.removeObserver(this);
            }
            lifecycleOwner.getLifecycle().addObserver(this);
            this.lifecycle = lifecycleOwner.getLifecycle();
        }
    }

    public final void unbindLifecycle() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
            this.lifecycle = null;
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
