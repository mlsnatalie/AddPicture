package com.ytx.appframework;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ytx.appframework.dialog.AFDialogListener;
import com.ytx.appframework.dialog.IAlertDialog;
import com.ytx.appframework.dialog.IConfirmDialog;
import com.ytx.appframework.dialog.LoadingDialog;
import com.ytx.appframework.dialog.SimpleDialog;
import com.ytx.appframework.event.AppLoginEvent;
import com.ytx.appframework.widget.TitleBar;
import com.ytx.mvpframework.presenter.ActivityPresenter;
import com.ytx.mvpframework.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhy on 16-8-10.
 */
@SuppressWarnings("checkstyle:all")
public abstract class BaseActivity<T extends ActivityPresenter> extends FragmentActivity
        implements ExitController.ExitControllerHandler {

    private StatusBarTintManager tintManager;
    private ExitController exitController = new ExitController();
    protected T presenter;
    protected TitleBar titleBar;

    private IAlertDialog alertDialog;
    private IConfirmDialog confirmDialog;
    private LoadingDialog loadingDialog;

    private boolean isDestroy;
    private boolean isResume;
    private boolean isStop;

    private boolean isAppLogin;
    private boolean isRegistered;

    /**
     * 权限授权回调
     */
    PermissionListener mPermissionListener;

    public final boolean isDestroy() {
        return isDestroy;
    }

    public final boolean isResume() {
        return isResume;
    }

    public final boolean isStop() {
        return isStop;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        isAppLogin = isAppLogin();
        initStatusBar();
        beforeCreatePresenter(savedInstanceState);
        presenter = createPresenter();
    }

    protected void beforeCreatePresenter(@Nullable Bundle savedInstanceState) {

    }

    protected T createPresenter() {
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectView();
        setDefaultLeftIconAction();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        injectView();
        setDefaultLeftIconAction();
    }

    protected void injectView() {
        View view = findViewById(R.id.title_bar);
        if (view instanceof TitleBar) {
            this.titleBar = (TitleBar) view;
        }
    }

    private void setDefaultLeftIconAction() {
        if (titleBar != null) {
            titleBar.setLeftIconAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleBack();
                }
            });
        }
    }

    public void handleBack() {
        exitController.handleBack(this);
    }

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

    private void initStatusBar() {
        if (isCustomStatusBar()) {
            tintManager = onInitStatusBar();
        }
    }

    protected StatusBarTintManager onInitStatusBar() {
        StatusBarTintManager tintManager = new StatusBarTintManager(this, isOverlay());
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
        return tintManager;
    }

    /**
     * 设置状态栏颜色
     * @param color
     */
    protected void setStatusBarColor(int color) {
        if (tintManager != null && tintManager.isStatusBarAvailable()) {
            tintManager.setStatusBarTintColor(color);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }
    }

    /**
     * 设置titlebar背景颜色
     */
    public void setTitleBgColor(@ColorInt int color) {
        if (titleBar != null) {
            titleBar.setTitleBarBgColor(color);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        setTitle(title, TitleBar.INVALID_INT_VALUE, TitleBar.INVALID_FLOAT_VALUE);
    }

    /**
     * 设置标题、颜色、字体大小
     */
    public void setTitle(String title, @ColorInt int color, float size) {
        if (titleBar != null) {
            titleBar.setTitle(title);
            titleBar.setTitleColor(color);
            titleBar.setTitleTextSize(size);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application application = getApplication();
        if (application != null && application instanceof IApplication) {
            ((IApplication) application).bindCurrentActivity(this);
        }
        this.isResume = true;
        verifyAppLogin();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            isRegistered = true;
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isResume = false;
        Application application = getApplication();
        if (application != null && application instanceof IApplication) {
            ((IApplication) application).unBindCurrentActivity(this);
        }
        if (isRegistered) {
            isRegistered = false;
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.isStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.isStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    /**
     * pushFragment方法强制containerViewId是R.id.fragment_container
     * 否则不要用这个方法，请使用FragmentUtils.pushFragment(...)方法
     */
    protected final void pushFragment(BaseFragment fragment, boolean isAddToBack) {
        FragmentUtils.pushFragment(getSupportFragmentManager(), fragment, isAddToBack);
    }

    protected final void pushFragment(BaseFragment fragment) {
        FragmentUtils.pushFragment(getSupportFragmentManager(), fragment, true);
    }

    protected final void pushFragment(BaseFragment fragment, String tag) {
        FragmentUtils.pushFragment(getSupportFragmentManager(), fragment, tag, true);
    }

    protected final void pushFragment(BaseFragment fragment, String tag, boolean allowStateLoss) {
        FragmentUtils.pushFragment(getSupportFragmentManager(), fragment, tag, true, allowStateLoss);
    }

    protected final void pushFragment(BaseFragment fragment, String tag, boolean isAddToBack, boolean allowStateLoss) {
        FragmentUtils.pushFragment(getSupportFragmentManager(), fragment, tag, isAddToBack, allowStateLoss);
    }

    private BaseFragment getCurrentFragment() {
        Fragment fragment = FragmentUtils.getCurrentFragment(getSupportFragmentManager());
        if (fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    protected final boolean popFragment() {
        ExitController.handleBackForFragment(this);
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        BaseFragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment.handleDispatchKeyEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        BaseFragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment.handleDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 应该返回是否mainActivity
     */
    protected boolean isMainActivity() {
        return false;
    }

    /**
     * @return true, 表示拦截 自己处理，false 表示有系统处理返回
     */
    @Override
    public boolean onHandleBack() {
        if (isMainActivity()) {
            exitController.handleRequestExit(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Hide soft keyboard.
     */
    protected void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            final InputMethodManager mgr =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    protected IAlertDialog createAlertDialog() {
        return new SimpleDialog(this);
    }

    protected IConfirmDialog createConfirmDialog() {
        return new SimpleDialog(this);
    }

    public void alert(String title, String message, AFDialogListener listener) {
        if (alertDialog == null) {
            alertDialog = createAlertDialog();
        }
        alertDialog.setTitleText(title);
        alertDialog.setContentText(message);
        alertDialog.setListener(listener);
        alertDialog.show();
    }

    public void alertNew(String title, String message, AFDialogListener listener) {
        IAlertDialog alertDialog = createAlertDialog();
        alertDialog.setTitleText(title);
        alertDialog.setContentText(message);
        alertDialog.setListener(listener);
        alertDialog.show();
    }

    public void confirm(String title, String message, AFDialogListener listener) {
        if (confirmDialog == null) {
            confirmDialog = createConfirmDialog();
        }
        confirmDialog.setTitleText(title);
        confirmDialog.setContentText(message);
        confirmDialog.setListener(listener);
        confirmDialog.show();
    }

    public void confirmNew(String title, String message, AFDialogListener listener) {
        IConfirmDialog confirmDialog = createConfirmDialog();
        confirmDialog.setTitleText(title);
        confirmDialog.setContentText(message);
        confirmDialog.setListener(listener);
        confirmDialog.show();
    }

    protected LoadingDialog createLoadingDialog() {
        return new LoadingDialog(this);
    }

    public void showLoading() {
        showLoading(getString(R.string.loading));
    }

    public void showLoading(String message) {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog();
        }
        loadingDialog.setMessage(message);
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected boolean isAppLogin() {
        return false;
    }


    /**
     * 权限申请
     * @param permissions
     * @param listener
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {

        //todo 获取栈顶activity，如果null。return；

        this.mPermissionListener = listener;

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else{
            listener.onGranted();
        }
    }

    /**
     * 申请结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    List<String> deniedPermissions = new ArrayList<>();
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED){
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){
                        mPermissionListener.onGranted();
                    }else{
                        mPermissionListener.onDenied(deniedPermissions);
                        mPermissionListener.onGranted(grantedPermissions);
                    }
                }
                break;
        }
    }
}
