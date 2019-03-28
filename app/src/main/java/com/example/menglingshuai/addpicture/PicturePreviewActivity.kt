package com.example.menglingshuai.addpicture

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.activity_picture_preview.*



class PicturePreviewActivity : BaseActivity<ActivityPresenter<*>>() {

    private var index: Int? = null

    companion object {
        const val EXTRA_PATH = "extra_path"
        const val EXTRA_PATH_INDEX = "extra_path_index"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_preview)

        title_bar.setTitleBarBgColor(resources.getColor(R.color.camera_bg_title_bar))

        val path = intent?.getStringExtra(EXTRA_PATH)
        index = intent?.getIntExtra(EXTRA_PATH_INDEX,1)

        if(path == null) {
            finish()
            return
        }
        Glide.with(this).load(path).into(iv_preview)

        tv_delete.setOnClickListener {
            val hashMap: HashMap<String, String> = hashMapOf()
            setResult(0, Intent().putExtra(EXTRA_PATH_INDEX, intent.getIntExtra(EXTRA_PATH_INDEX, -1)))
            finish()
        }

        tv_confirm.setOnClickListener {
            finish()
        }
    }

    override fun onHandleBack(): Boolean {
        return super.onHandleBack()
    }
}
