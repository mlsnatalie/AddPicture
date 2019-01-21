package com.ytx.mvpframework.presenter;

import com.ytx.mvpframework.view.IView;

public class ViewDelegatePresenter<V extends IView> extends BasePresenter<V> {

    private boolean isBinded = false;
    private boolean isViewCreated = false;

    public ViewDelegatePresenter(V view) {
        super(view);
    }

    public void onViewCreated() {
        isViewCreated = true;
    }

    public void onDestroyView() {
        isViewCreated = false;
    }

    public void onBind() {
        isBinded = true;
    }

    public void onUnBind() {
        isBinded = false;
    }

    public final boolean isViewCreated() {
        return isViewCreated;
    }

    public final boolean isBinded() {
        return isBinded;
    }
}
