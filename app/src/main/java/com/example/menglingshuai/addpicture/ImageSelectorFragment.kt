package com.example.menglingshuai.addpicture


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.menglingshuai.addpicture.imagefileselector.ImageFileSelector
import kotlinx.android.synthetic.main.fragment_image_selector.*

/**
 * 拍照 或 从相册选择图片
 */
class ImageSelectorFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun create(fm: FragmentManager, isAllowMulti:Boolean = false): ImageSelectorFragment {
            var fragment = fm.findFragmentByTag(ImageSelectorFragment::class.java.simpleName)
            if (fragment !is ImageSelectorFragment) {
                fragment = ImageSelectorFragment()
            }
            fragment.fm = fm
            fragment.isAllowMulti = isAllowMulti
            return fragment
        }
    }

    private lateinit var fm:FragmentManager
    private var selector: ImageFileSelector? = null
    var success: ((filePaths:Array<String>?) -> Unit)? = null
    var fail: (() -> Unit)? = null
    private var isAllowMulti = false

    private var isAddCamereShow: Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context == null) {
            throw IllegalStateException("fragment must attach context")
        }

        if (selector == null) {
            selector = ImageFileSelector(context, isAllowMulti)
        }
        selector?.enableAllowMultiple(isAllowMulti)
        selector?.setCallback((object : ImageFileSelector.Callback {
            override fun onSuccess(p0: Array<String>?) {
                dismiss()
                success?.invoke(p0)
            }

            override fun onError() {
                dismiss()
                fail?.invoke()
            }
        }))

//        permissionChecker = PermissionChecker(activity!!, arrayOf(Manifest.permission.CAMERA), arrayOf())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selector = ImageFileSelector(context, isAllowMulti).apply {
            setCallback(object : ImageFileSelector.Callback{
                override fun onSuccess(p0: Array<String>?) {
                    success?.invoke(p0)
                    dismiss()
                }

                override fun onError() {
                    fail?.invoke()
                    dismiss()
                }
            })
        }

        tv_capture_image.setOnClickListener {
            selector?.takePhoto(activity)
        }
        tv_select_from_gallery.setOnClickListener {
            selector?.selectImage(activity)
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
    }

//    fun show() {
//        show(fm, ImageSelectorFragment::class.java.simpleName)
//    }

    fun show(success: ((imagePath: Array<String>?) -> Unit)? = null, fail: (() -> Unit)? = null, isAllowMulti: Boolean = false) {
        this.success = success
        this.fail = fail
        this.isAllowMulti = isAllowMulti
        show(fm, ImageSelectorFragment::class.java.simpleName)
    }

    fun onActivityResult(activity: Activity, stopped: Boolean, requestCode: Int, resultCode: Int, data: Intent?) {
        selector?.onActivityResult(activity, stopped, requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog.window.setGravity(Gravity.BOTTOM)
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 129f, context?.resources?.displayMetrics).toInt()
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }
    }

    override fun getTheme(): Int {
        return R.style.TagDialog
    }
}
