package com.example.menglingshuai.addpicture.animation

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.example.menglingshuai.addpicture.R
import com.ytx.appframework.BaseActivity
import com.ytx.mvpframework.presenter.ActivityPresenter
import kotlinx.android.synthetic.main.in_out_activity.*

class InOutAnimationActivity : BaseActivity<ActivityPresenter<*>>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_out_activity)
        ll_first.setOnClickListener {
            //从左边移出
            ll_first.animation = AnimationUtils.makeOutAnimation(this, false)
            ll_first.visibility = View.GONE
        }
        ll_second.setOnClickListener {
            //从左边移走
            ll_second.animation = AnimationUtils.makeInAnimation(this, false)
            ll_second.visibility = View.GONE
        }

        ll_third.setOnClickListener {
            ll_third.animation = AnimationUtil.moveToViewBottom()
            ll_third.visibility = View.GONE
        }

        ll_fourth.setOnClickListener {
            ll_fourth.animation = AnimationUtil.moveToViewLocation()
            ll_fourth.visibility = View.GONE
        }
    }
}