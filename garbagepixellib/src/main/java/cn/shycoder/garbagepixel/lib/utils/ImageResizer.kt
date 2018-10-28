package cn.shycoder.garbagepixel.lib.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileDescriptor
import java.net.CacheRequest
import java.util.*


/**
 * 用于获取压缩后的Bitmap的工具类
 * */
internal object ImageResizer {

    fun fromResourceId(resources: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        val inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeResource(resources, resId, options)
    }

    fun fromFileDescriptor(fd: FileDescriptor, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fd, null, options)
        val inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeFileDescriptor(fd, null, options)
    }

    /**
     * 根据ImageView的宽和高来计算采样率
     * */
    private fun calculateInSampleSize(options: BitmapFactory.Options, requestWidth: Int, requestHeight: Int): Int {
        var inSample = 1
        val outWidth = options.outWidth
        val outHeight = options.outHeight
        if (outWidth > requestWidth * 2 && outHeight > requestHeight * 2) {
            while (true) {
                if (outWidth / inSample > requestWidth && outHeight / inSample > outHeight) {
                    inSample++
                } else {
                    break
                }
            }
        }
        return inSample
    }
}