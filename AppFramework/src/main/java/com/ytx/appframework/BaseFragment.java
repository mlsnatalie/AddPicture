package com.ytx.appframework;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ytx.appframework.dialog.AFDialogListener;
import com.ytx.appframework.event.AppLoginEvent;
import com.ytx.appframework.widget.TitleBar;
import com.ytx.mvpframework.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by rjhy on 16-8-10.
 */
@SuppressWarnings("checkstyle:all")
public abstract class BaseFragment<T extends BaseFragmentPresenter> extends Fragment
        implements IView, ExitController.ExitControllerHandler {

    private StatusBarTintManager tintManager;
    protected T presenter;
    protected TitleBar titleBar;

    private boolean isAppLogin;

    private boolean isDestroyView;
    private boolean isDestroy;
    private boolean isResume;
    private boolean isStop;

    private boolean isRegistered;

    /**
     * 是否用view代替系统的statusbar，模拟statusbar
     * 会添加WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS属性
     */
    protected boolean isCustomStatusBar() {
        return false;
    }

    /**
     * 当isCustomStatusBar 返回true时 才有效
     * statusbar 是否覆盖在图片的上满，比如图片置顶，statusbar透明
     */
    protected boolean isOverlay() {
        return false;
    }

    private boolean initStatusBar(View view) {
        if (isCustomStatusBar()) {
            tintManager = onInitStatusBar(view);
            return tintManager != null && tintManager.isStatusBarAvailable();
        }
        return false;
    }

    protected StatusBarTintManager onInitStatusBar(View view) {
        StatusBarTintManager tintManager = new StatusBarTintManager(view, isOverlay());
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
        return tintManager;
    }

    protected final void setStatusBarColor(int color) {
        if (tintManager != null && tintManager.isStatusBarAvailable()) {
            tintManager.setStatusBarTintColor(color);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(color);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        beforeCreatePresenter(savedInstanceState);
        presenter = createPresenter();
    }

    protected void beforeCreatePresenter(Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isDestroyView = false;
        if (getLayoutResource() != 0) {
            View view = inflater.inflate(getLayoutResource(), container, false);
            if (initStatusBar(view) && tintManager.getRootView().getParent() == null) {
                return tintManager.getRootView();
            } else {
                return view;
            }
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * 如果需要使用自定义状态栏，则一定要用这个方法设置布局
     */
    protected int getLayoutResource() {
        return 0;
    }

    public T createPresenter() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isAppLogin = isAppLogin();
        isDestroyView = false;
        View v = view.findViewById(R.id.title_bar);
        if (v instanceof TitleBar) {
            titleBar = (TitleBar) v;
            titleBar.setLeftIconAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleBack();
                }
            });
        }
        if (presenter != null) {
            presenter.onViewCreated(this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (presenter != null) {
            presenter.setUserVisibleHint(isVisibleToUser, isAdded());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
        if (presenter != null) {
            presenter.onDestroyView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
        verifyAppLogin();
        if (!EventBus.getDefault().isRegistered(this)) {
            isRegistered = true;
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
        if (isRegistered) {
            isRegistered = false;
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (presenter != null) {
            presenter.onHiddenChanged(hidden, isAdded());
        }
    }

    public void onStart() {
        super.onStart();
        isStop = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isStop = true;
    }

    /**
     * 是否处理key分发事件
     * @param event
     * @return false: 不处理DispatchKeyEvent
     */
    public boolean handleDispatchKeyEvent(KeyEvent event) {
        return false;
    }

    /**
     * 是否处理key分发事件
     * @param event
     * @return false: 不处理DispatchKeyEvent
     */
    public boolean handleDispatchTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 不要手动调这个方法，请调 handleBack 方法
     */
    @Override
    public boolean onHandleBack() {
        ExitController.handleBackForFragment(getActivity());
        return true;
    }

    public void handleBack() {
        ExitController.handleBackForFragment(getActivity());
    }

    /**
     * Hide soft keyboard.
     */
    protected void hideSoftKeyboard() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity.getCurrentFocus() != null) {
            final InputMethodManager mgr =
                    (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取当前页面的名称，类似activity的title
     */
    public String getPageName() {
        if (titleBar != null && titleBar.getTvTitle() != null) {
            return titleBar.getTvTitle().getText().toString();
        }
        return null;
    }

    private void verifyAppLogin() {
        boolean isAppLogin = isAppLogin();
        if (this.isAppLogin != isAppLogin) {
            this.isAppLogin = isAppLogin;
            if (isAppLogin) {
                onAppLogin();
            } else {
                onAppLogout();
            }
        }
    }

    /**
     * 需要Register EventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void onLoginEvent(AppLoginEvent event) {
        if (isResume()) {
            isAppLogin = event.isLogin;
            if (event.isLogin) {
                onAppLogin();
            } else {
                onAppLogout();
            }
        }
    }

    protected void onAppLogin() {

    }

    protected void onAppLogout() {

    }

    public void alert(String title, String message, AFDialogListener listener) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).alert(title, message, listener);
        }
    }

    public void alertNew(String title, String message, AFDialogListener listener) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).alertNew(title, message, listener);
        }
    }

    public void confirm(String title, String message, AFDialogListener listener) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).confirm(title, message, listener);
        }
    }

    public void confirmNew(String title, String message, AFDialogListener listener) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).confirmNew(title, message, listener);
        }
    }

    public void showLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    public void showLoading(String message) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading(message);
        }
    }

    protected boolean isAppLogin() {
        return false;
    }

    public final boolean isDestroyView() {
        return isDestroyView;
    }

    public final boolean isDestroy() {
        return isDestroy;
    }

    public final boolean isResume() {
        return isResume;
    }

    public final boolean isStop() {
        return isStop;
    }
}
