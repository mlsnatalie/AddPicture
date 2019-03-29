package com.example.menglingshuai.addpicture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.menglingshuai.addpicture.utils.DisplayUtil.dp2px
import com.example.menglingshuai.addpicture.utils.FileUtils
import com.example.menglingshuai.addpicture.utils.ToastUtils
import com.yalantis.ucrop.UCrop
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.activity_ucrop.*
import java.io.File
import java.util.*

private const val REQUEST_CODE_ADD_PICTURE = 33

class UcropActivity : BaseActivity<ActivityPresenter<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucrop)
        to_ucrop_gallery.setOnClickListener { addPicture() }
    }

    private fun addPicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_ADD_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_PICTURE) {
                if (data?.data != null) {
                    UCrop.of(data.data, Uri.fromFile(File(cacheDir, UUID.randomUUID().toString() +  ".png")))
                        .withAspectRatio(1f, 1f)
                        .withOptions(UCrop.Options().apply {
                            setHideBottomControls(true)
                            setStatusBarColor(resources.getColor(R.color.camera_bg_title_bar))
                            setToolbarColor(resources.getColor(R.color.camera_bg_title_bar))
                        })
                        .withMaxResultSize(dp2px(this,300), dp2px(this,300))
                        .start(this)
//            val filePath = FileUtils.getFilePathByUri(this, data.data)
//            updateLiveCover(filePath)
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                val resultUri = UCrop.getOutput(data!!)
                val filePath = FileUtils.getFilePathByUri(this, resultUri)
                updateLiveCover(filePath)
            }
        }
    }

    private fun updateLiveCover(filePath: String?) {
//        this.liveRoomInfo!!.cover = filePath
        Glide.with(this)
            .load(filePath)
            .apply(
                RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .override(
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140f, resources.displayMetrics).toInt(), TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics).toInt())).into(iv_cover)

    }


}