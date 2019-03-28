package com.example.menglingshuai.addpicture

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.menglingshuai.addpicture.utils.DescHelper
import com.example.menglingshuai.addpicture.utils.ToastUtils
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.activity_crop.*

class CropActivity : BaseActivity<ActivityPresenter<*>>() {

    private val imageSelector by lazy {
        ImageSelectorFragment.create(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        to_open_gallery.setOnClickListener {
            imageSelector.show(success = { imagePaths ->
                onCoverChanged(imagePaths?.get(0))
            },
                fail = {
                    ToastUtils.showToastOfEWD(this, "修改封面失败")
                })
        }
    }

    private fun onCoverChanged(srcPath: String?) {

        // 处理并保存
        val localCoverPath = DescHelper.handleCoverBitmap(BitmapFactory.decodeFile(srcPath), srcPath!!)
        // 展示
        showCover(srcPath)
    }

    private fun showCover(path: String) {
        val str1 = "/storage/emulated/0/Android/data/com.example.menglingshuai.addpicture/cache/image/image_selector/img_1553763779658.jpg"
        val str2 = "/storage/emulated/0/Pictures/gpzs_img/399012afee44872ca7da2bd5729f9b94.jpg"

        val str3 = "/storage/emulated/0/Android/data/cn.com.spero.elderwand/cache/image/image_selector/img_1553762593037.jpg"
        val str4 = "/storage/emulated/0/Pictures/gpzs_img/d4ae91f9c40f176317f593359ee89101.jpg"
        val str5 = "/storage/emulated/0/Pictures/gpzs_img/397d1e7739268901b77e70f0eb75d0fa.jpg"
        Glide.with(this).load(path)
            .into(iv_cover)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageSelector.onActivityResult(this, false, requestCode, resultCode, data)
    }
}