package cn.shycoder.garbagepixel.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class Pixel private constructor(private val mAppliationContext: Context, val executor: Executor, val cache: Cache) {

    /**
     * 运行于 UI 线程的Handler
     * Bitmap将会通过此Handler更新到ImageView上
     * */
    val HANDLER = object : android.os.Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }

    /**
     * ImageView 和 Action的映射
     * */
    val mTargetActionMap: Map<ImageView, Action> = hashMapOf()


    fun from(url: URL) {

    }

    fun from(resId: Int) {
    }

    companion object {
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
        private lateinit var executor: Executor

        /**
         * 缓存
         * */
        private lateinit var cache: Cache


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
                    maxSize,
                    minSize,
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
            TODO()
        }

    }
}