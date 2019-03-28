package com.example.menglingshuai.addpicture.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.io.File

/**
 * created by zhangliang on 2018/12/18
 * profile: zhangliangnbu@163.com
 * 视频描述、分享页公共模块
 */
object DescHelper {

    private const val TITLE_MAX_LENGTH = 30
    /**
     * 初始化视频标题
     */
    fun initVideoTitleEditText(context: Context, et: EditText, sensorClickCallback: (() -> Unit)? = null) {
        et.isCursorVisible = false
        et.setOnClickListener {
            sensorClickCallback?.invoke()
            et.isCursorVisible = true
        }
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = et.text
                val index = et.selectionStart
                if (text.length > TITLE_MAX_LENGTH) {
                    ToastUtils.showToastOfEWD(context, "视频标题字数最多30字")
                    var start = index - (text.length - TITLE_MAX_LENGTH)
                    if (start < 0) {
                        start = 0
                    }
                    val end = start + text.length - TITLE_MAX_LENGTH
                    text.delete(start, end)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    // 截取、压缩、保存
    fun handleCoverBitmap(srcBitmap: Bitmap, srcPath: String): String {

        val requireRatio = 240.0 / 135 // 图片布局大小 h:w
        val srcRatio = srcBitmap.height * 1.0 / srcBitmap.width
        val oppositeRequireRatio = 135.0 / 240
        val maxByteSize = 1 * 1024 * 1024L
        // 是否符合标准。符合标准则不做处理，考虑到精度损失，放宽比较条件
        if (srcRatio in ((requireRatio - 0.01)..(requireRatio + 0.01)) && srcBitmap.width * srcBitmap.height * 4 <= maxByteSize && fromFilePath(srcPath)?.type == "image") {
            return srcPath
        }

        // 截取。太宽了就截掉左面右面部分，留中间
        val cropBitmap = BitmapUtils.cropBitmapByWidth(srcBitmap, oppositeRequireRatio)
        // 截取。太高了就截掉上面下面部分，留中间
        val sCropBitmap = BitmapUtils.cropBitmap(cropBitmap, requireRatio)

        // 压缩。不大于1M
        val compressBitmap = BitmapUtils.compressByScale(sCropBitmap, maxByteSize)

        // 保存
        return BitmapUtils.saveBitmap(compressBitmap, getOrCreateCoverFile(srcPath))
    }

    private fun getOrCreateCoverFile(srcPath: String): File {
        val parentDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile, "gpzs_img")
        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }
        val coverName = MessageDigetUtil.md5(srcPath) + ".jpg"
        return File(parentDir, coverName)
    }

    /**
     * 本地资源，网络资源不判断
     */
    fun fromFilePath(filePath: String): MediaType? {
        val index = filePath.lastIndexOf(".")
        if (index != -1) {
            val suffix = filePath.substring(index)
            return MediaType.values().find { it.suffix.equals(suffix, true) }
        }
        return null
    }
}