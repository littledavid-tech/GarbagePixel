package cn.shycoder.garbagepixel.lib

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import cn.shycoder.garbagepixel.lib.utils.Utils

class Request private constructor(
        private val pixel: Pixel,
        val source: Any,
        val loadFrom: LoadFrom,
        val placeholderDrawable: Drawable?,
        val errorDrawable: Drawable?,
        val requestWidth: Int,
        val requestHeight: Int) {

    private val TAG = Request::class.java.name

    val bitmapKey: BitmapKey = generateKey()

    /**
     * 生成一个唯一标识的Key
     * */
    private fun generateKey(): BitmapKey {
        val buffer = StringBuffer()
        //标识
        val originalKey = Utils.decodeMD5(source.toString())
        buffer.append(originalKey)
        //大小参数
        buffer.append("size_")
        buffer.append("w_${requestWidth}_")
        buffer.append("h_$requestHeight")
        val processedKey = buffer.toString()
        //打印 BitmapKey
        if (pixel.isDebugging()) {
            Log.i(TAG, "Generated processedKey:$processedKey")
        }
        return BitmapKey(originalKey, processedKey)
    }

    class RequestBuilder constructor(private val pixel: Pixel, private val source: Any, val dispatcher: Dispatcher) {
        private var mLoadFrom: LoadFrom = LoadFrom.ANY
        private var mPlaceholderDrawable: Drawable? = null
        private var mErrorDrawable: Drawable? = null

        fun loadFrom(loadFrom: LoadFrom): RequestBuilder {
            mLoadFrom = loadFrom
            return this
        }

        /**
         * 设置ImageView的占位符Drawable,当加载Bitmap之前，显示在ImageView的Drawable
         * */
        fun placeholder(drawable: Drawable): RequestBuilder {
            this.mPlaceholderDrawable = drawable
            return this
        }

        /**
         * 设置ImageView的占位符Drawable,当加载Bitmap之前，显示在ImageView的Drawable
         * */
        fun placeholder(drawableId: Int): RequestBuilder {
            try {
                val drawable = pixel.context.resources.getDrawable(drawableId)
                this.mPlaceholderDrawable = drawable
                return this
            } catch (ex: Resources.NotFoundException) {
                mPlaceholderDrawable = null
                ex.printStackTrace()
                return this
            }
        }

        /**
         * 设置当发生错误的时候显示的Drawable
         * @param drawableId drawable 的资源ID
         * */
        fun error(drawableId: Int): RequestBuilder {
            try {
                val drawable = pixel.context.resources.getDrawable(drawableId)
                this.mErrorDrawable = drawable
                return this
            } catch (ex: Resources.NotFoundException) {
                mErrorDrawable = null
                ex.printStackTrace()
                return this
            }
        }

        /**
         * 设置当发生错误的时候显示的Drawable
         * */
        fun error(drawable: Drawable): RequestBuilder {
            this.mErrorDrawable = drawable
            return this
        }

        /**
         * 将指定的Bitmap加载到ImageView上
         * */
        fun into(target: ImageView) {
            val measuredWidth = target.measuredWidth
            val measuredHeight = target.measuredHeight
            val request = Request(pixel,
                    source,
                    mLoadFrom,
                    mPlaceholderDrawable,
                    mErrorDrawable,
                    measuredWidth,
                    measuredHeight)
            dispatcher.dispatchSubmit(Action(request.source, target, request))
        }
    }
}