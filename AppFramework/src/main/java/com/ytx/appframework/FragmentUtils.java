package com.ytx.appframework;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * 目标getFragment工具类
 */
public class FragmentUtils {

    private static final int DEFAULT_FRAGMENT_CONTAINER_ID = R.id.fragment_container;

    /**
     * hide fragment
     * enter anim is bottom to top
     * exit anim is top to bottom
     */
    public static void hideFragmentOfTopToBottom(FragmentManager fm, Fragment hideFragment) {
        hideFragment(fm, hideFragment, R.anim.bottom_to_top, R.anim.top_to_bottom);
    }

    /**
     * add and show fragment
     * enter anim is bottom to top
     * exit anim is top to bottom
     */
    public static void showFragmentOfBottomToTop(FragmentManager fm, Fragment showFragment) {
        showFragment(fm, showFragment, null, DEFAULT_FRAGMENT_CONTAINER_ID,
                showFragment.getClass().getName(), R.anim.bottom_to_top, R.anim.top_to_bottom);
    }

    /**
     * hide fragment
     */
    public static void hideFragment(FragmentManager fm, Fragment hideFragment) {
        hideFragment(fm, hideFragment, null, null);
    }

    /**
     * hide fragment
     */
    public static void hideFragment(FragmentManager fm, Fragment hideFragment, @AnimRes Integer enter,
                                    @AnimRes Integer exit) {
        showFragment(fm, null, hideFragment, DEFAULT_FRAGMENT_CONTAINER_ID, null, enter, exit);
    }

    /** add and show fragment */
    public static void showFragment(FragmentManager fm, Fragment showFragment) {
        showFragment(fm, showFragment, DEFAULT_FRAGMENT_CONTAINER_ID);
    }

    /** add and show fragment */
    public static void showFragment(FragmentManager fm, Fragment showFragment,
                                    @IdRes int containerViewId) {
        showFragment(fm, showFragment, null, containerViewId, showFragment.getClass().getName());
    }

    /** add and show fragment */
    public static void showFragment(FragmentManager fm, Fragment showFragment,
                                    Fragment hideFragment, @IdRes int containerViewId, String tag) {
        showFragment(fm, showFragment, hideFragment, containerViewId, tag, null, null);
    }

    /**
     * add and show fragment
     */
    public static void showFragment(FragmentManager fm, Fragment showFragment,
                                    Fragment hideFragment, @IdRes int containerViewId, String tag, @AnimRes Integer enter,
                                    @AnimRes Integer exit) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (enter != null && exit != null) {
            fragmentTransaction.setCustomAnimations(enter, exit);
        }
        if (showFragment != null) {
            if (fm.findFragmentByTag(tag) != null) {
                showFragment = fm.findFragmentByTag(tag);
            } else {
                fragmentTransaction.add(containerViewId, showFragment, tag);
            }
            fragmentTransaction.show(showFragment);
        }
        if (hideFragment != null) {
            fragmentTransaction.hide(hideFragment);
        }
        fragmentTransaction.commit();
    }

    public static void pushFragment(FragmentManager fm, @IdRes int containerViewId, Fragment fragment, String tag, boolean addToBack, boolean allowStateLoss, @AnimRes Integer enter, @AnimRes Integer exit, @AnimRes Integer popEnter, @AnimRes Integer popExit) {
        FragmentTransaction ft = fm.beginTransaction();
        if (enter != null && exit != null) {
            ft.setCustomAnimations(enter, exit, popEnter == null ? 0 : popEnter, popExit == null ? 0 : popExit);
        }
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (TextUtils.isEmpty(tag)) {
            ft.replace(containerViewId, fragment);
        } else {
            ft.replace(containerViewId, fragment, tag);
        }
        if (addToBack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        if (allowStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
        fm.executePendingTransactions();
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, @IdRes int containerViewId, Fragment fragment, String tag, boolean addToBack, boolean allowStateLoss) {
        pushFragment(fm, containerViewId, fragment, tag, addToBack, allowStateLoss, null, null, null, null);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, @IdRes int containerViewId,
                                    Fragment fragment) {
        pushFragment(fm, containerViewId, fragment, null, false, false);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, Fragment fragment, boolean addToBack) {
        pushFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID, fragment, null, addToBack, false);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, Fragment fragment) {
        pushFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID, fragment, null, false, false);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, Fragment fragment, String tag) {
        pushFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID, fragment, tag, false, false);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, Fragment fragment, String tag,
                                    boolean addToBack) {
        pushFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID, fragment, tag, addToBack, false);
    }

    /**
     * fragment进栈
     */
    public static void pushFragment(FragmentManager fm, BaseFragment fragment, String tag,
                                    boolean addToBack, boolean allowStateLoss) {
        pushFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID, fragment, tag, addToBack, allowStateLoss);
    }

    /**
     * fragment出栈
     *
     * @return true:操作成功
     */
    public static boolean popFragment(FragmentManager fm) {
        final int entryCount = fm.getBackStackEntryCount();
        FragmentTransaction ft = fm.beginTransaction();
        boolean popSucceed = true;
        if (entryCount <= 1) {
            fm.popBackStack();
        } else {
            popSucceed = fm.popBackStackImmediate();
        }
        ft.commit();
        return popSucceed;
    }

    /**
     * 获取当前fragment
     *
     * @return 当前fragment
     */
    public static Fragment getCurrentFragment(FragmentManager fm) {
        return getCurrentFragment(fm, DEFAULT_FRAGMENT_CONTAINER_ID);
    }

    /**
     * 获取当前fragment
     *
     * @return 当前fragment
     */
    public static Fragment getCurrentFragment(FragmentManager fm, @IdRes int id) {
        return fm.findFragmentById(id);
    }

    /**
     * 获取当前fragment
     *
     * @return 当前fragment
     */
    public static Fragment getCurrentFragment(FragmentManager fm, String tag) {
        return fm.findFragmentByTag(tag);
    }
}