//@file:JvmName("ActivityPresenter")
//package com.ytx.mvpframework.presenter
//
//import android.support.v4.app.FragmentActivity
//import com.ytx.mvpframework.view.IView
//
//open class ActivityPresenter<V : IView>(view: V) : LifecyclePresenter<V>(view) {
//
//    init {
//        if (view is FragmentActivity) {
//            bindActivity(view)
//        }
//    }
//
//    public fun bindActivity(activity: FragmentActivity) {
//        bindLifecycle(activity)
//    }
//
//    public fun unbindActivity() {
//        unbindLifecycle()
//    }
//}