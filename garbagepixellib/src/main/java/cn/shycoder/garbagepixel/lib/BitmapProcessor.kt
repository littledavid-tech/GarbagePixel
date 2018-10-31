package cn.shycoder.garbagepixel.lib

import android.graphics.Bitmap
import android.util.Log
import cn.shycoder.garbagepixel.lib.processors.DrawableBitmapProcessor
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
    private val TAG = BitmapProcessor::class.java.name

    var exception: Exception? = null
    var result: Bitmap? = null

    override fun run() {
        try {
            if (pixel.isDebugging()) {
                Log.i(TAG, "Start thread to decode bitmap!")
            }
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

    /**
     * 优先地从缓存中加载Bitmap，如果没有则会调用 #decode() 方法从来源加载Bitmap
     * */
    private fun loadBitmap(): Bitmap? {
        if (pixel.isDebugging()) {
            Log.i(TAG, "Load bitmap from cache!")
        }
        //直接从来源获取Bitmap
        if (request.loadFrom == LoadFrom.SOURCE) {
            return decode()
        }
        //从缓存中获取Bitmap
        var bitmap: Bitmap? = null
        val bitmapKey = request.bitmapKey
        //从内存中取出Bitmap
        bitmap = cache.get(bitmapKey.processedKey)
        if (bitmap != null) {
            return bitmap
        }
        //检查缓存中是否具有原图
        if (cache.isExistedInDiskCache(bitmapKey.originalKey)) {
            //根据原图加载缩略图
            val fileInputStream = FileInputStream(cache.getFD(bitmapKey.originalKey))
            val fd = fileInputStream.fd
            val inSampledBitmap = ImageResizer.fromFileDescriptor(fd, request.requestWidth, request.requestHeight)
            cache.put(request.bitmapKey.originalKey, inSampledBitmap)
            fileInputStream.safeClose()
            return inSampledBitmap
        }
        return decode()
    }

    /**
     * 从Bitmap的来源直接解析Bitmap
     * */
    abstract fun decode(): Bitmap?

    companion object {
        /**
         * 根据数据的来源，创建不同的Processor来加载Bitmap
         * */
        fun create(pixel: Pixel, dispatcher: Dispatcher, action: Action): BitmapProcessor {
            if (action.source is String) {
                return NetworkBitmapProcessor(pixel, dispatcher, action.request, action, pixel.cache)
            } else if (action.source is Int) {
                return DrawableBitmapProcessor(pixel, dispatcher, action.request, action, pixel.cache)
            }
            throw  IllegalArgumentException("Unknown bitmap source: action.source")
        }
    }
}

