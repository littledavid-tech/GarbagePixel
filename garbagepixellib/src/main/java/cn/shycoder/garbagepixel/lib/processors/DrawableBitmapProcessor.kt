package cn.shycoder.garbagepixel.lib.processors

import android.content.res.Resources
import android.graphics.Bitmap
import cn.shycoder.garbagepixel.lib.*
import cn.shycoder.garbagepixel.lib.utils.ImageResizer

/**
 * Created by ITSoftware on 10/31/2018.
 */
class DrawableBitmapProcessor(pixel: Pixel,
                              dispatcher: Dispatcher,
                              request: Request,
                              action: Action,
                              cache: Cache)
    : BitmapProcessor(pixel, dispatcher, request, action, cache) {

    override fun decode(): Bitmap? {
        val resources = pixel.context.resources
        val id = request.source.toString().toInt()
        return decodeResource(resources, id)
    }

    private fun decodeResource(resources: Resources, drawableResId: Int): Bitmap {
        val bitmap = ImageResizer.fromResourceId(resources, drawableResId,
                request.requestWidth, request.requestHeight)
        cache.put(request.bitmapKey.processedKey, bitmap)
        return bitmap
    }
}