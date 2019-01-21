//@file:JvmName("BasePresenter")
//package com.ytx.mvpframework.presenter
//
//import android.arch.lifecycle.LifecycleObserver
//import com.ytx.mvpframework.view.IView
//import rx.Subscription
//import rx.subscriptions.CompositeSubscription
//
//abstract class BasePresenter<V : IView>(val view: V) : LifecycleObserver {
//    private var compositeSubscription: CompositeSubscription? = null
//
//    fun addSubscription(subscription: Subscription) {
//        if (compositeSubscription == null || compositeSubscription!!.isUnsubscribed) {
//            synchronized(1) {
//                if (compositeSubscription == null || compositeSubscription!!.isUnsubscribed) {
//                    compositeSubscription = CompositeSubscription()
//                }
//            }
//        }
//        compositeSubscription!!.add(subscription)
//    }
//
//    fun unSubscribe() {
//        compositeSubscription?.unsubscribe()
//    }
//}