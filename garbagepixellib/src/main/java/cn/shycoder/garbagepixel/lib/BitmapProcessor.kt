package cn.shycoder.garbagepixel.lib

import android.graphics.Bitmap

/**
 * Bitmap的处理者
 * */
internal abstract class BitmapProcessor(
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
                dispatcher.dispatchFailed(action)
            } else {
                dispatcher.dispatchCompelelate(action)
            }
        } catch (ex: Exception) {
            exception = ex
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
            return null
        }
        return null
    }

    abstract fun decode(): Bitmap?

    companion object {
        fun create(pixel: Pixel, dispatcher: Dispatcher, action: Action, request: Request): BitmapProcessor {
            TODO()
        }
    }
}
