package com.example.menglingshuai.addpicture.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.TypedValue
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * created by zhangliang on 2018/11/27
 * profile: zhangliangnbu@163.com
 */
object BitmapUtils {

    fun compressByScale(src: Bitmap, maxByteSize: Long): Bitmap {

        // 每次压缩比例
        val scale = 0.9f

        // 计算最终的缩放比
        var tempHeight = src.height * 1.0f
        var tempWidth = src.width * 1.0f
        while (tempHeight * tempWidth * 4 > maxByteSize) {
            tempHeight *= scale
            tempWidth *= scale
        }
        val finalHeightScale = tempHeight / src.height
        val finalWidthScale = tempWidth / src.width
        val matrix = Matrix()
        matrix.setScale(finalWidthScale, finalHeightScale)

        // 生成bitmap
        val finalBitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
//        if(!src.isRecycled) {
//            src.recycle()
//        }
        return finalBitmap
    }

    /**
     * ratio height / width
     */
    fun cropBitmap(srcBitmap: Bitmap, ratio: Double): Bitmap {
        val srcRatio = srcBitmap.height * 1.0 / srcBitmap.width
        // 截取。太高了就截掉上面下面部分，留中间
        return if (srcRatio <= ratio) {
            srcBitmap
        } else {
            val dh = (srcRatio - ratio) * srcBitmap.width
            Bitmap.createBitmap(srcBitmap, 0, (dh / 2).toInt(), srcBitmap.width, (srcBitmap.height - dh).toInt())
        }
    }

    fun cropBitmapByWidth(srcBitmap: Bitmap, ratio: Double): Bitmap {
        val srcRatio = srcBitmap.width * 1.0 / srcBitmap.height
        // 截取。太宽了就截掉左面右面部分，留中间
        return if (srcRatio <= ratio) {
            srcBitmap
        } else {
            val dh = (srcRatio - ratio) * srcBitmap.height
            Bitmap.createBitmap(srcBitmap, (dh / 2).toInt(), 0, (srcBitmap.width - dh).toInt(), srcBitmap.height)
        }
    }

    fun dealWithMiniBitmap(context: Context, srcBitmap: Bitmap): Bitmap {
        var width =(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, context.resources.displayMetrics)).toInt()
        val imageScale = width * 1f / srcBitmap.width
        val minHeight = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160f, context.resources.displayMetrics) / imageScale).toInt()
        val matrix = Matrix()
        matrix.postScale(imageScale, imageScale)
        var isCropHeight = false
        if (minHeight < srcBitmap.height) {
            isCropHeight = true
        }
        var y = if(isCropHeight) (srcBitmap.height * 0.1f).toInt() else 0
        var height = Math.min(srcBitmap.height - y, minHeight)
        var bitmap = Bitmap.createBitmap(srcBitmap, 0, y, srcBitmap.width, height, matrix, true)
//        if (!srcBitmap.isRecycled) {
//            srcBitmap.recycle()
//        }
        return bitmap
    }


    fun saveBitmap(bmp: Bitmap, targetFile:File): String {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(targetFile)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile.path
    }
}