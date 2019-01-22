package com.example.menglingshuai.addpicture

import android.Manifest
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.menglingshuai.addpicture.utils.DisplayUtil
import com.example.menglingshuai.addpicture.utils.ToastUtils
import com.ytx.appframework.BaseActivity
import com.ytx.appframework.PermissionListener
import kotlinx.android.synthetic.main.activity_addpicture.*

class AddPictureActivity : BaseActivity<PreparePicturePresenter>(), PreparePictureView, PermissionListener {

    companion object {
        private const val REQUEST_CODE_ADD_PICTURE = 1001
        private const val REQUEST_CODE_DELETE_PICTURE = 1002
    }

    private val pictureAdapter by lazy {
        PictureAdapter(this, mutableListOf(),
            addCallback = { addPicture() },
            itemClick = { src, index -> previewPicture(src, index) })
    }

    private val imageSelector by lazy {
        ImageSelectorFragment.create(supportFragmentManager, true).apply {
            success = { imagePaths ->
                if (imagePaths != null) {
                    presenter.addPicturesFromGallery(imagePaths)
                }
            }
            fail = {
                ToastUtils.showToastOfEWD(this@AddPictureActivity, "增加图片失败")
            }
        }
    }

    private var isActivityStopped = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpicture)

        setStatusBarColor(resources.getColor(R.color.camera_bg_title_bar))

        title_bar_add.setTitleBarBgColor(resources.getColor(R.color.camera_bg_title_bar))
        val array = arrayOfNulls<String>(1)
        array[0] = Manifest.permission.READ_EXTERNAL_STORAGE
        requestRunTimePermission(array, this)

//        requestReadPhoneState()
        title_bar_add.setLeftIconAction {
            //title_bar左侧的监听动作
            handleBack()
        }

        /**
         * 动态处理标题栏右侧图标的大小
         */
        title_bar_add.ivRight.run {

            layoutParams.width = DisplayUtil.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f)
            layoutParams.height = DisplayUtil.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f)
            minimumHeight = 0
            minimumWidth = 0
            scaleType = ImageView.ScaleType.FIT_CENTER
            (layoutParams as? LinearLayout.LayoutParams)?.gravity = Gravity.CENTER
            (layoutParams as? LinearLayout.LayoutParams)?.rightMargin = 36
        }

        rv_pictures.apply {
            layoutManager = GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(AddPictureActivity.EvenItemDecoration(DisplayUtil.dp2px(context, 3), 4))
            adapter = pictureAdapter
        }

        tv_delete_all.setOnClickListener {
            presenter.deleteAllPictures()
        }
    }

//    private fun requestReadPhoneState() {
//
//        PermissionsUtil.requestPermission(this, object : PermissionListener {
//            override fun permissionDenied(permission: Array<out String>) {
//                Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
//            }
//
//            override fun permissionGranted(permission: Array<out String>) {
//                Toast.makeText(this@MainActivity, "用户拒绝了访问内部存储", Toast.LENGTH_LONG).show()
//            }
//
//        }, Manifest.permission.READ_EXTERNAL_STORAGE)
//    }


    override fun onResume() {
        super.onResume()
        isActivityStopped = false
    }

    override fun onStop() {
        super.onStop()
        isActivityStopped = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DELETE_PICTURE -> presenter.deletePictureFromPreview(data)
            else -> {
            }
        }
        imageSelector.onActivityResult(this, isActivityStopped, requestCode, resultCode, data)
//        when(requestCode) {
//
//            REQUEST_CODE_ADD_PICTURE -> presenter.addPicturesFromGallery(data)
//            REQUEST_CODE_DELETE_PICTURE -> presenter.deletePictureFromPreview(data)
//            else -> {}
//        }
    }

    private fun addPicture() {
        imageSelector.show()
    }

//    private fun addPicture() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_ADD_PICTURE)
//    }

    private fun previewPicture(src: String, index: Int) {
        val intent = Intent(this, PicturePreviewActivity::class.java)
            .putExtra(PicturePreviewActivity.EXTRA_PATH, src)
            .putExtra(PicturePreviewActivity.EXTRA_PATH_INDEX, index)
        startActivityForResult(intent, REQUEST_CODE_DELETE_PICTURE)
    }

    override fun createPresenter(): PreparePicturePresenter {
        return PreparePicturePresenter(this)
    }

    override fun showPictures(srcs: List<String>) {
        pictureAdapter.items = srcs
        pictureAdapter.notifyDataSetChanged()
    }

    override fun showAddPictureError(message: String) {
        ToastUtils.showToastOfEWD(this, message)
    }

    override fun showPictureAmountLimitPrompt(visibility: Int) {
        tv_picture_amount_limit.visibility = visibility
    }

    override fun showDeleteAllButton(visibility: Int) {
        tv_delete_all.visibility = visibility
    }

    override fun getAddiblePictureCount(): Int {
        return pictureAdapter.getAddibleCount()
    }

    override fun getPictureCount(): Int {
        return pictureAdapter.items.size
    }

    class EvenItemDecoration(val space: Int, val column: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            // 每个span分配的间隔大小
            val spanSpace = space * (column + 1) / column
            // 列索引
            val colIndex = position % column
            // 列左、右间隙
            outRect.left = space * (colIndex + 1) - spanSpace * colIndex
            outRect.right = spanSpace * (colIndex + 1) - space * (colIndex + 1)
            // 行间距
            if (position >= column) {
                outRect.top = space
            }
        }
    }

    override fun onGranted() {
//        Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
    }

    override fun onGranted(grantedPermission: MutableList<String>?) {
//        Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
    }

    override fun onDenied(deniedPermission: MutableList<String>?) {
    }

    override fun handleBack() {
        super.handleBack()
    }
}