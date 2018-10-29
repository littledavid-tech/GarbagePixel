package cn.shycoder.garbagepixel.lib.processors

import android.graphics.Bitmap
import cn.shycoder.garbagepixel.lib.*

/**
 * Created by ITSoftware on 10/29/2018.
 */
internal class NetworkBitmapProcessor
(pixel: Pixel, dispatcher: Dispatcher, request: Request, action: Action, cache: Cache) :
        BitmapProcessor(pixel, dispatcher, request, action, cache) {
    override fun decode(): Bitmap? {
        TODO()
    }

    fun downloadBitmapFromNetwork() {
        TODO()
    }
}