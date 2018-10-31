package cn.shycoder.garbagepixel.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Looper
import android.os.Message
import android.support.v4.app.NotificationCompatBase
import android.util.Log
import android.widget.ImageView
import java.net.URL
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.acos


class Pixel private constructor(val context: Context,
                                val executor: ThreadPoolExecutor,
                                val cache: Cache) {

    private val TAG = Pixel::class.java.name

    /**
     * 运行于 UI 线程的Handler
     * Bitmap将会通过此Handler更新到ImageView上
     * */
    private val HANDLER = object : android.os.Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {

            when (msg!!.what) {
                MSG_ACTION_COMPELETE -> {//加载Bitmap成功
                    val bitmapProcessor = msg.obj as BitmapProcessor
                    if (bitmapProcessor.action.target.get() != null) {
                        val iv = bitmapProcessor.action.target.get()!!
                        //停止帧动画
                        closeAnimation(iv.drawable)
                        //进行来源的判断，判断是否有ImageView错位的现象
                        val obj = mTargetActionMap.get(bitmapProcessor.action.target.get())
                                ?: return
                        if (bitmapProcessor.action.source.toString() != obj.source.toString()) {
                            Log.e(TAG, "解决错位问题!")
                            return
                        }
                        if (bitmapProcessor.result != null) {
                            iv.setImageBitmap(bitmapProcessor.result)
                        }
                    }
                }
                MSG_ACTION_FAILED -> {//加载Bitmap失败
                    val bitmapProcessor = msg.obj as BitmapProcessor
                    if (bitmapProcessor.action.target.get() != null) {
                        val iv = bitmapProcessor.action.target.get()!!
                        //停止帧动画
                        closeAnimation(iv.drawable)
                        //如果设置了加载失败后的Drawable，则加载此Drawable
                        if (bitmapProcessor.action.request.errorDrawable != null) {
                            iv.setImageDrawable(bitmapProcessor.action.request.errorDrawable)
                        }
                    }
                    if (isDebugging()) {
                        Log.e(TAG, "A bitmap happened Source:${bitmapProcessor.request.source}")
                        bitmapProcessor.exception?.printStackTrace()
                    }
                }
                else -> {
                    throw  IllegalArgumentException("Unknown message!")
                }
            }
        }

        /**
         * 停止帧动画
         * */
        private fun closeAnimation(drawable: Drawable?) {
            if (drawable != null && drawable is AnimationDrawable) {
                drawable.stop()
            }
        }
    }

    private val dispatcher: Dispatcher = Dispatcher(this, context, executor, HANDLER, cache)
    /**
     * ImageView 和 Action的映射
     * */
    private val mTargetActionMap: WeakHashMap<ImageView, Action> = WeakHashMap()

    fun from(url: String): Request.RequestBuilder {
        return Request.RequestBuilder(this, url, this.dispatcher)
    }

    fun from(resId: Int): Request.RequestBuilder {
        return Request.RequestBuilder(this, resId, this.dispatcher)
    }

    fun isDebugging(): Boolean {
        return false
    }

    internal fun put(imageView: ImageView?, action: Action?) {
        this.mTargetActionMap.put(imageView, action)
    }

    companion object {
        val MSG_ACTION_COMPELETE = 0
        val MSG_ACTION_FAILED = 1

        @SuppressLint("StaticFieldLeak")
        private var mInstance: Pixel? = null

        fun with(context: Context): Pixel {
            if (mInstance == null) {
                mInstance = Builder(context).build()
            }
            return mInstance!!
        }

        fun with(activity: Activity): Pixel {
            if (mInstance == null) {
                mInstance = Builder(activity.applicationContext).build()
            }
            return mInstance!!
        }

        fun with(fragment: Fragment): Pixel {
            if (mInstance == null) {
                mInstance = Builder(fragment.activity.applicationContext).build()
            }
            return mInstance!!
        }
    }

    /**
     * Pixel 的建造器
     * */
    private class Builder(private val context: Context) {

        /**
         * 线程池
         * */
        private lateinit var executor: ThreadPoolExecutor

        /**
         * 缓存
         * */
        private lateinit var cache: Cache

        /**
         * 调度者
         * */
        private lateinit var dispatcher: Dispatcher

        /**
         * 初始化线程池
         * */
        fun initExecutor() {
            //获取虚拟的可用CPU核心数
            val cpuCoreCount = Runtime.getRuntime().availableProcessors()
            val minSize = cpuCoreCount + 1
            val maxSize = cpuCoreCount * 2 + 1
            //初始化线程池
            executor = ThreadPoolExecutor(
                    minSize,
                    maxSize,
                    10L,
                    TimeUnit.SECONDS,
                    LinkedBlockingDeque<Runnable>())
        }

        /**
         * 初始化缓存
         * */
        fun initCache() {
            cache = Cache.getInstance(context)
        }

        /**
         * 构建Pixel对象
         * */
        fun build(): Pixel {
            initExecutor()
            initCache()
            mInstance = Pixel(this.context, this.executor, this.cache)
            return mInstance!!
        }

    }
}