package com.example.menglingshuai.addpicture

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_camera_picture_prepare.view.*
import kotlinx.android.synthetic.main.item_camera_picture_prepare_add.view.*

class PictureAdapter(private val activity: FragmentActivity,
                     var items: List<String> = listOf(),
                     private val addCallback: () -> Unit,
                     private val itemClick:(path:String, index:Int) -> Unit)
    : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_ADD = 1
        private const val MAX_COUNT_LIMIT = 11
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = when (viewType) {
            TYPE_IMAGE -> R.layout.item_camera_picture_prepare
            TYPE_ADD -> R.layout.item_camera_picture_prepare_add
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

        val view = LayoutInflater.from(parent.context!!).inflate(layoutId, parent, false)

        // 动态设置图片大小
        val ivSize = (DisplayUtil.getScreenContentWidth(activity) - DisplayUtil.dp2px(activity, 3) * 5) / 4
        val iv = when(viewType) {
            TYPE_IMAGE -> view.iv_picture
            TYPE_ADD -> view.cl_add_picture
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
        iv.clipToOutline = true
        iv.layoutParams.height = ivSize

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_IMAGE -> {
                val item = items[position]
                holder.view.apply {
                    Glide.with(this).load(item).into(iv_picture)
                    iv_picture.setOnClickListener {
                      itemClick(item, position)
                    }
                }
            }
            TYPE_ADD -> {
                holder.view.setOnClickListener {
                    addCallback()
                }
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

    }

    override fun getItemCount(): Int {
        return if (items.size < MAX_COUNT_LIMIT) {
            items.size + 1
        } else {
            MAX_COUNT_LIMIT
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.size < MAX_COUNT_LIMIT && position >= items.size) {
            TYPE_ADD
        } else {
            TYPE_IMAGE
        }
    }

    fun getAddibleCount():Int {
        return MAX_COUNT_LIMIT - items.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}