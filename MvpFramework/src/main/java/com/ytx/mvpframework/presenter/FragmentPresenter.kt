//@file:JvmName("FragmentPresenter")
//package com.ytx.mvpframework.presenter
//
//import android.arch.lifecycle.LifecycleOwner
//import android.support.v4.app.Fragment
//import com.ytx.mvpframework.view.IView
//
//open class FragmentPresenter<V : IView>(view: V) : LifecyclePresenter<V>(view) {
//    private var isViewCreated: Boolean = false
//
//    init {
//        if (view is Fragment) {
//            bindFragment(view)
//        }
//    }
//
//    public fun bindFragment(view: Fragment) {
//        bindLifecycle(view)
//    }
//
//    public fun unbindFragment() {
//        unbindLifecycle()
//    }
//
//    open fun onViewCreated(owner: LifecycleOwner) {
//        isViewCreated = true
//    }
//
//    open fun onDestroyView(owner: LifecycleOwner) {
//        isViewCreated = false
//    }
//
//    fun isViewCreated() : Boolean {
//        return isViewCreated
//    }
//}