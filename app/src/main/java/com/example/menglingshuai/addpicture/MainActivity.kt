package com.example.menglingshuai.addpicture

import android.content.Intent
import android.os.Bundle
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityPresenter<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title_bar.setTitleBarBgColor(resources.getColor(R.color.camera_bg_title_bar))
        to_add_picture.setOnClickListener { startActivity(Intent(this, AddPictureActivity::class.java)) }
        crop_camera.setOnClickListener {startActivity(Intent(this, CropActivity::class.java)) }
        ucrop_picture.setOnClickListener { startActivity(Intent(this, UcropActivity::class.java)) }
    }

}
