package com.example.menglingshuai.addpicture

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.view.View
import com.ytx.mvpframework.presenter.ActivityPresenter
import com.ytx.mvpframework.view.IView

interface PreparePictureView : IView {
    fun showPictures(srcs: List<String>)
    fun showAddPictureError(message: String)
    fun showPictureAmountLimitPrompt(visibility: Int)
    fun showDeleteAllButton(visibility: Int)
    fun getAddiblePictureCount(): Int
    fun getPictureCount():Int
}

class PreparePicturePresenter(view: PreparePictureView) : ActivityPresenter<PreparePictureView>(view) {

    private val liveData: MutableLiveData<List<String>> = MutableLiveData()

    /** 添加的图片 */
    private val pictures: MutableList<String> = mutableListOf()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        liveData.value = pictures.toList()
        liveData.observe(owner, Observer {
            view.showPictures(it!!)

            view.showPictureAmountLimitPrompt(if (view.getAddiblePictureCount() <= 0) {
                View.VISIBLE
            } else {
                View.GONE
            })

            view.showDeleteAllButton(if (view.getPictureCount() > 0) {
                View.VISIBLE
            } else {
                View.GONE
            })

        })
    }

    fun addPicturesFromGallery(data: Array<String>?) {
        if (data == null || data.isEmpty()) {
            return view.showAddPictureError("没有选择图片或选择图片失败")
        }
        if (data.size == 1) {
            addPicture(data[0])
            liveData.value = pictures.toList()
        } else if (view.getAddiblePictureCount() > 0) {
            val ims = mutableListOf<String>()
            for (i in 0..Math.min(data.size - 1, view.getAddiblePictureCount() - 1)) {
                ims.add(data[i])
            }
            addPicture(ims)
            liveData.value = pictures.toList()
        }
    }


    fun addPicturesFromGallery(data: Intent?) {
        if (data?.data == null && (data?.clipData == null || data.clipData.itemCount <= 0)) {
            return view.showAddPictureError("没有选择图片或选择图片失败")
        }
        if (data.data != null) {
            addPicture(FileUtils.getFilePathByUri(view as Context, data.data))
            liveData.value = pictures.toList()
        } else if (view.getAddiblePictureCount() > 0) {
            val ims = mutableListOf<String>()
            for (i in 0..Math.min(data.clipData.itemCount - 1, view.getAddiblePictureCount() - 1)) {
                ims.add(FileUtils.getFilePathByUri(view as Context, data.clipData.getItemAt(i).uri))
            }
            addPicture(ims)
            liveData.value = pictures.toList()
        }
    }

    fun deletePictureFromPreview(data: Intent?) {
        val index = data?.getIntExtra(PicturePreviewActivity.EXTRA_PATH_INDEX, -1) ?: return
        if (index < 0) {
            return
        }
        removePicture(index)
        liveData.value = pictures.toList()
    }

    fun deleteAllPictures() {
        clearPicture()
        liveData.value = pictures.toList()
    }

    fun addPicture(src: String) {
        pictures.add(src)
    }

    fun addPicture(src: List<String>) {
        pictures.addAll(src)
    }

    fun removePicture(index: Int) {
        pictures.removeAt(index)
    }

    fun clearPicture() {
        pictures.clear()
    }
}

