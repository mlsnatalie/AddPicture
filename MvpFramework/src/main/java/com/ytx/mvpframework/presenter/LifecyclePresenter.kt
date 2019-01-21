//@file:JvmName("LifecyclePresenter")
//package com.ytx.mvpframework.presenter
//
//import android.arch.lifecycle.*
//import com.ytx.mvpframework.view.IView
//
///**
// * 建议结合LiveData使用
// * 在onCreate方法中创建LiveData
// */
//open class LifecyclePresenter<V : IView>(view: V) : BasePresenter<V>(view), LifecycleObserver {
//    private var lifecycle: Lifecycle? = null
//    private var isDestroy: Boolean = false
//
//    fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
//        if (lifecycleOwner.lifecycle != lifecycle) {
//            lifecycle?.removeObserver(this)
//            lifecycleOwner.lifecycle.addObserver(this)
//            this.lifecycle = lifecycleOwner.lifecycle
//        }
//    }
//
//    fun unbindLifecycle() {
//        this.lifecycle?.removeObserver(this)
//        this.lifecycle = null
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    open fun onCreate(owner: LifecycleOwner) {
//        isDestroy = false
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    open fun onStart(owner: LifecycleOwner) {
//
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    open fun onResume(owner: LifecycleOwner) {
//
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    open fun onPause(owner: LifecycleOwner) {
//
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    open fun onStop(owner: LifecycleOwner) {
//
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    open fun onDestroy(owner: LifecycleOwner) {
//        isDestroy = true
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
//    open fun onAny(owner: LifecycleOwner) {
//
//    }
//
//    fun isCreated() : Boolean {
//        return if (lifecycle != null) {
//            lifecycle!!.currentState.isAtLeast(Lifecycle.State.CREATED)
//        } else {
//            false
//        }
//    }
//
//    fun isResume() : Boolean {
//        return if (lifecycle != null) {
//            lifecycle!!.currentState.isAtLeast(Lifecycle.State.RESUMED)
//        } else {
//            false
//        }
//    }
//
//    fun isStart() : Boolean  {
//        return if (lifecycle != null) {
//            lifecycle!!.currentState.isAtLeast(Lifecycle.State.STARTED)
//        } else {
//            false
//        }
//    }
//
//    fun isDestroy() : Boolean {
//        return isDestroy
//    }
//}