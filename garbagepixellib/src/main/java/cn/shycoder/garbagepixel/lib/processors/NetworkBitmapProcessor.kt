package cn.shycoder.garbagepixel.lib.processors

import android.graphics.Bitmap
import android.nfc.Tag
import android.util.Log
import cn.shycoder.garbagepixel.lib.*
import cn.shycoder.garbagepixel.lib.utils.ImageResizer
import cn.shycoder.garbagepixel.lib.utils.Utils
import cn.shycoder.garbagepixel.lib.utils.safeClose
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL

/**
 * Created by ITSoftware on 10/29/2018.
 */
internal class NetworkBitmapProcessor
(pixel: Pixel, dispatcher: Dispatcher, request: Request, action: Action, cache: Cache) :
        BitmapProcessor(pixel, dispatcher, request, action, cache) {

    private val TAG = NetworkBitmapProcessor::class.java.name

    override fun decode(): Bitmap? {

        if (pixel.isDebugging()) {
            Log.i(TAG, "decode from network!${action.source.toString()}")
        }
        //检查网络状态,此方法需要 访问网络状态的权限
//        if (!Utils.networkAvailable(pixel.context)) {
//            return null
//        }
        try {
            return decodeStream(URL(request.source.toString()).openStream())
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw  ex
        }
    }

    private fun decodeStream(inputStream: InputStream): Bitmap? {
        if (pixel.isDebugging()) {
            Log.i(TAG, "Decode from network stream!")
        }
        //将从网络中下载的图片放入磁盘缓存中
        if (pixel.isDebugging()) {
            Log.i(TAG, "put bitmap into cache")
        }
        val originalKey = request.bitmapKey.originalKey
        cache.put(originalKey, inputStream)
        inputStream.safeClose()

        //判断磁盘缓存中是否存在指定的文件
        if (!cache.isExistedInDiskCache(originalKey)) {
            Log.e(TAG, "Failed to store bitmap into disk cache")
            return null
        }
        if (pixel.isDebugging()) {
            Log.i(TAG, "Bitmap has been putted into disk cache!")
        }
        val fd = cache.getFD(originalKey)
        val bitmap = ImageResizer.fromFileDescriptor(fd, request.requestWidth, request.requestHeight)
        cache.put(request.bitmapKey.processedKey, bitmap)
        return bitmap
    }
}