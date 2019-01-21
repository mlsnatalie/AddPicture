package com.example.menglingshuai.addpicture

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.Field

/**
 * created by zhangliang on 2018/11/8
 * profile: zhangliangnbu@163.com
 */
object ToastUtils {

    // 文本的toast
    private var mTextToast: Toast? = null

    // 自定义view的 toast
    private var mViewToast: Toast? = null


    /*****  Android 7.0  toast crash  解决方案: http://www.10tiao.com/html/223/201801/2651232846/1.html   */
    private var sField_TN: Field? = null
    private var sField_TN_Handler: Field? = null

    init {
        try {
            sField_TN = Toast::class.java.getDeclaredField("mTN")
            sField_TN!!.isAccessible = true
            sField_TN_Handler = sField_TN!!.type.getDeclaredField("mHandler")
            sField_TN_Handler!!.isAccessible = true
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun showToastOfEWD(context: Context?, msg: String) {
        if (context == null) {
            return
        }
        if (TextUtils.isEmpty(msg)) {
            return
        }

        if (mTextToast == null) {
            mTextToast = Toast(context.applicationContext)
            mTextToast!!.duration = Toast.LENGTH_SHORT
            mTextToast!!.setGravity(Gravity.CENTER, 0, 0)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater ?: return
            val view = inflater.inflate(R.layout.toast_center_black, null)
            val tv = view.findViewById<TextView>(R.id.tv_content)
            tv.text = msg
            mTextToast!!.view = view

            // 如果是7.0 才去hook
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                hook(mTextToast)
            }
        }
        mTextToast!!.view.findViewById<TextView>(R.id.tv_content)?.text = msg
        mTextToast!!.show()
    }


    private fun hook(toast: Toast?) {
        try {
            val tn = sField_TN!!.get(toast)
            val preHandler = sField_TN_Handler!!.get(tn) as Handler
            sField_TN_Handler!!.set(tn, SafelyHandlerWarpper(preHandler))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getToast(): Toast? {
        return if (mTextToast == null) mViewToast else mTextToast
    }

    fun releaseInstance() {
        mTextToast = null
        mViewToast = null
    }


    private class SafelyHandlerWarpper(private val impl: Handler) : Handler() {

        override fun handleMessage(msg: Message) {
            impl.handleMessage(msg)//需要委托给原Handler执行
        }

        override fun dispatchMessage(msg: Message) {
            try {
                super.dispatchMessage(msg)
            } catch (e: Exception) {
            }

        }
    }
}