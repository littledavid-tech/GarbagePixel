package cn.shycoder.garbagepixel.lib.processors

import android.graphics.Bitmap
import cn.shycoder.garbagepixel.lib.*
import cn.shycoder.garbagepixel.lib.utils.ImageResizer
import cn.shycoder.garbagepixel.lib.utils.Utils
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
    override fun decode(): Bitmap? {
        if (!Utils.networkAvailable(pixel.context)) {
            return null
        }
        try {
            return decodeStream(URL(request.source.toString()).openStream())
        } catch (ex: Exception) {
            throw  ex
        }
    }

    fun decodeStream(inputStream: InputStream): Bitmap? {
        //将从网络中下载的图片放入磁盘缓存中
        val originalKey = request.bitmapKey.originalKey
        cache.put(originalKey, inputStream)
        inputStream.close()
        //判断磁盘缓存中是否存在指定的文件
        if (!cache.isExistedInDiskCache(originalKey)) {
            return null
        }

        val fd = cache.getFD(originalKey)
        val bitmap = ImageResizer.fromFileDescriptor(fd, request.requestWidth, request.requestHeight)
        cache.put(request.bitmapKey.processedKey, bitmap)
        return bitmap
    }
}