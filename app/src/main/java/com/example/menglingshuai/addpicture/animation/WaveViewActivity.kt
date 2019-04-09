package com.example.menglingshuai.addpicture.animation

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import com.example.menglingshuai.addpicture.R
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.wave_view_activity.*

class WaveViewActivity : BaseActivity<ActivityPresenter<*>>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wave_view_activity)

        wave_view.setDuration(5000)
        wave_view.setStyle(Paint.Style.STROKE)
        wave_view.setColor(Color.WHITE)
        wave_view.setInterpolator(LinearOutSlowInInterpolator())
        wave_view.start()

        wave_view.postDelayed({
            wave_view.stop()
        }, 10000)
    }
}