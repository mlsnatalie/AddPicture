package com.ytx.appframework;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * created by rjhy on 16-8-30
 */
public class ExitController {

    private static final int TIME_GAP = 5000;
    private long lastBackEventTime;

    public ExitController() {

    }

    private boolean requestExit(Activity activity) {
        long currentTime = System.currentTimeMillis();
        if (lastBackEventTime == 0 || currentTime <= lastBackEventTime
                || (currentTime - lastBackEventTime) >= TIME_GAP) {
            lastBackEventTime = currentTime;
            showToast(activity, activity.getString(R.string.exit));
            return false;
        }

        try {
            return true;
        } finally {
            lastBackEventTime = 0;
        }
    }

    private void showToast(Context context, String content) {
        if (content != null) {
            Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    /**
     * 处理退出请求
     * @param activity
     */
    public void handleRequestExit(Activity activity) {
        if (requestExit(activity)) {
            activity.finish();
        }
    }

    /**
     * 处理返回事件
     */
    public void handleBack(Activity activity) {
        hideSoftInput(activity);
        if (activity instanceof FragmentActivity) {
            exitForFragment((FragmentActivity) activity);
        } else {
            exitForActivity(activity);
        }
    }

    /**
     * 处理fragment的返回事件
     * @param activity
     */
    public static void handleBackForFragment(FragmentActivity activity) {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            FragmentUtils.popFragment(activity.getSupportFragmentManager());
        } else {
            exitForActivity(activity);
        }
    }

    private void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static void exitForFragment(FragmentActivity activity) {
        Fragment fragment = FragmentUtils.getCurrentFragment(activity.getSupportFragmentManager());
        if (fragment != null) {
            if (fragment instanceof ExitControllerHandler) {
                if (!((ExitControllerHandler) fragment).onHandleBack()) {
                    handleBackForFragment(activity);
                }
            } else {
                handleBackForFragment(activity);
            }
        } else {
            handleBackForFragment(activity);
        }
    }

    private static void exitForActivity(Activity activity) {
        if (activity instanceof ExitControllerHandler) {
            if (!((ExitControllerHandler) activity).onHandleBack()) {
                activity.finish();
            }
        } else {
            activity.finish();
        }
    }

    /**
     * 退出控制回调
     */
    public static interface ExitControllerHandler {
        /**
         * 处理返回键
         * @return true: 处理返回事件
         */
        boolean onHandleBack();
    }
}
