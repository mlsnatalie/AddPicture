package com.ytx.appframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhy on 16-8-9.
 *
 * LazyFragment 只能嵌套在activity或LazyFragment中，否则可能会有bug
 */
@SuppressWarnings("checkstyle:all")
public abstract class LazyFragment<T extends LazyFragmentPresenter> extends BaseFragment<T> {

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isVisible = false;
    private List<FragmentVisibleListener> fragmentVisibleListeners = new ArrayList<>();

    private FragmentVisibleListener listener = new FragmentVisibleListener() {
        @Override
        public void onVisibleChanged(boolean isVisible) {
            setVisibleToUser(isVisible);
        }
    };

    final void addVisibleListener(FragmentVisibleListener listener) {
        if (!fragmentVisibleListeners.contains(listener)) {
            fragmentVisibleListeners.add(listener);
        }
    }

    final void removeVisibleListener(FragmentVisibleListener listener) {
        fragmentVisibleListeners.remove(listener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParentVisibleListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeParentVisibleListener();
    }

    private void initParentVisibleListener() {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof LazyFragment) {
            ((LazyFragment) fragment).addVisibleListener(listener);
        }
    }

    private void removeParentVisibleListener() {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof LazyFragment) {
            ((LazyFragment) fragment).removeVisibleListener(listener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        setVisibleToUser(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setVisibleToUser(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setVisibleToUser(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setVisibleToUser(!hidden);
    }

    private boolean checkVisible() {
        Fragment fragment = getParentFragment();
        while (fragment != null) {
            if (fragment.isHidden() || !fragment.getUserVisibleHint()) {
                return false;
            }
            fragment = fragment.getParentFragment();
        }
        return !isHidden() && getUserVisibleHint();
    }

    /**
     * set visible to User
     * @param isVisibleToUser
     */
    public void setVisibleToUser(boolean isVisibleToUser) {
        if (getView() != null) {
            if (isVisibleToUser) {
                if (!isVisible && checkVisible()) {
                    if (isFirstVisible) {
                        isFirstVisible = false;
                        callFirstUserVisible();
                    } else {
                        onUserVisible();
                    }
                    isVisible = isVisibleToUser;
                    notifyFragmentVisibleListeners(isVisibleToUser);
                }
            } else {
                if (isVisible) {
                    if (isFirstInvisible) {
                        isFirstInvisible = false;
                        callFirstUserInvisible();
                    } else {
                        onUserInvisible();
                    }
                    notifyFragmentVisibleListeners(isVisibleToUser);
                }
                isVisible = isVisibleToUser;
            }
        }
    }

    private void notifyFragmentVisibleListeners(boolean isVisible) {
        for (FragmentVisibleListener listener : fragmentVisibleListeners) {
            listener.onVisibleChanged(isVisible);
        }
    }

    private void callFirstUserVisible() {
        onFirstUserVisible();
        onUserVisible();
    }

    protected void onFirstUserVisible() {
        createLazyLayout(getView());
        if (presenter != null) {
            presenter.onFirstUserVisible();
        }
    }

    /**
     * 此方法可用于创建的不可见的fragment在创建实例的时候仅仅创建根布局容器，在第一次可见的时候动态创建内容。
     * 例如在ViewPager的offscreenPageLimit大于1的时候，用于优化第一次加载的性能。
     * @param rootView
     */
    protected void createLazyLayout(@Nullable View rootView) {

    }

    protected void onUserVisible() {
        if (presenter != null) {
            presenter.onUserVisible();
        }
    }

    private void callFirstUserInvisible() {
        onUserInvisible();
        onFirstUserInvisible();
    }

    protected void onFirstUserInvisible() {
        if (presenter != null) {
            presenter.onFirstUserInvisible();
        }
    }

    protected void onUserInvisible() {
        if (presenter != null) {
            presenter.onUserInvisible();
        }
    }

    private static interface FragmentVisibleListener {
        /**
         * visibility变化回调
         * @param isVisible
         */
        void onVisibleChanged(boolean isVisible);
    }
}
