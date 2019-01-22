package com.example.menglingshuai.addpicture

import android.content.Intent
import android.os.Bundle
import com.ytx.appframework.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<PreparePicturePresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title_bar.setTitleBarBgColor(resources.getColor(R.color.camera_bg_title_bar))
        to_add_picture.setOnClickListener { startActivity(Intent(this, AddPictureActivity::class.java)) }
    }
}
