package com.ytx.appframework;

import com.ytx.mvpframework.view.IView;

/**
 * Created by linxi on 2018/3/13.
 */

public class LazyFragmentPresenter<V extends IView> extends BaseFragmentPresenter<V> {
    /** 对应 onUserVisible状态 */
    private boolean isUserVisible;

    public LazyFragmentPresenter(V view) {
        super(view);
    }

    public void onFirstUserVisible() {

    }

    protected void onUserVisible() {
        isUserVisible = true;
    }

    public void onFirstUserInvisible() {

    }

    protected void onUserInvisible() {
        this.isUserVisible = false;
    }

    /**
     * 对应onUserVisible状态
     */
    public boolean isUserVisible() {
        return isUserVisible;
    }
}
