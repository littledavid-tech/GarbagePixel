package cn.shycoder.garbagepixel.lib

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.util.Log.i
import kotlin.math.min

/**
 * Created by ITSoftware on 10/27/2018.
 */
class Pixel private constructor(private val mAppliationContext: Context) {

    companion object {
        /**
         * 缓存策略选项 不使用缓存
         * */
        val CACHE_OPTION_NONE = 0
        /**
         * 缓存策略选项 仅从内存缓存中读取
         * */
        val CACHE_OPTION_MEMORY = 1
        /**
         * 缓存策略选项
         * */
        val CACHE_OPTION_ALL = 2


        private var mInstance: Pixel? = null

        fun with(context: Context): Pixel {
            if (mInstance == null) {
                mInstance = Pixel(context.applicationContext)
            }
            return mInstance!!
        }

        fun with(activity: Activity): Pixel {
            if (mInstance == null) {
                mInstance = Pixel(activity.applicationContext)
            }
            return mInstance!!
        }

        fun with(fragment: Fragment): Pixel {
            if (mInstance == null) {
                mInstance = Pixel(fragment.activity.applicationContext)
            }
            return mInstance!!
        }
    }
}