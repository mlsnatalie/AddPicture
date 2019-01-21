package com.ytx.appframework;

import com.ytx.mvpframework.presenter.FragmentPresenter;
import com.ytx.mvpframework.view.IView;

/**
 * Created by fangcan on 2018/3/12.
 */

public class BaseFragmentPresenter<V extends IView> extends FragmentPresenter<V> {

    /** 对应 onHiddenChanged状态 */
    private boolean isHidden;
    /** 对应 setUserVisibleHint状态 */
    private boolean isVisibleToUser;

    public BaseFragmentPresenter(V view) {
        super(view);
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    /**
     * 对应fragment onHiddenChanged回调
     * @param hidden
     * @param isAdded fragment 是否添加到activity里了
     */
    protected void onHiddenChanged(boolean hidden, boolean isAdded) {
        this.isHidden = hidden;
    }

    /**
     * 对应fragment setUserVisibleHint回调
     * @param isVisibleToUser
     * @param isAdded fragment 是否添加到activity里了
     */
    protected void setUserVisibleHint(boolean isVisibleToUser, boolean isAdded) {
       this.isVisibleToUser = isVisibleToUser;
    }
}
