package cn.shycoder.garbagepixel.lib

import android.graphics.Bitmap
import cn.shycoder.garbagepixel.lib.processors.NetworkBitmapProcessor
import cn.shycoder.garbagepixel.lib.utils.ImageResizer
import cn.shycoder.garbagepixel.lib.utils.safeClose
import java.io.FileInputStream

/**
 * Bitmap的处理者
 * */
abstract class BitmapProcessor(
        val pixel: Pixel,
        val dispatcher: Dispatcher,
        val request: Request,
        val action: Action,
        val cache: Cache) : Runnable {

    var exception: Exception? = null
    var result: Bitmap? = null

    override fun run() {
        try {
            result = loadBitmap()
            //加载Bitmap失败
            if (result == null) {
                dispatcher.dispatchFailed(this)
            } else {
                dispatcher.dispatchComplete(this)
            }
        } catch (ex: Exception) {
            exception = ex
            dispatcher.dispatchFailed(this)
        }
    }

    private fun loadBitmap(): Bitmap? {
        //直接从来源获取Bitmap
        if (request.loadFrom == LoadFrom.SOURCE) {
            return decode()
        }
        //从缓存中获取Bitmap
        var bitmap: Bitmap? = null
        val bitmapKey = request.bitmapKey
        //从内存中取出Bitmap
        bitmap = cache.get(bitmapKey.processedKey)
        if (bitmap == null) {
            //检查缓存中是否具有原图
            if (cache.isExistedInDiskCache(bitmapKey.originalKey)) {
                //根据原图加载缩略图
                val fileInputStream = FileInputStream(cache.toString())
                val fd = fileInputStream.fd
                val inSampledBitmap = ImageResizer.fromFileDescriptor(fd, request.requestWidth, request.requestHeight)
                cache.put(request.bitmapKey.originalKey, inSampledBitmap)
                fileInputStream.safeClose()
                return inSampledBitmap
            }
        }
        return decode()
    }

    abstract fun decode(): Bitmap?

    companion object {
        fun create(pixel: Pixel, dispatcher: Dispatcher, action: Action): BitmapProcessor {
            if (action.source is String) {
                return NetworkBitmapProcessor(pixel, dispatcher, action.request, action, pixel.cache)
            }
            throw  IllegalArgumentException("Unknown bitmap source: action.source")
        }
    }
}

