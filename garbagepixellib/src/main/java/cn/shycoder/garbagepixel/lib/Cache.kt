package cn.shycoder.garbagepixel.lib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.renderscript.RSRuntimeException
import android.support.v4.util.LruCache
import cn.shycoder.garbagepixel.lib.utils.safeClose
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File
import java.io.InputStream

/**
 * Created by ITSoftware on 10/27/2018.
 */
class Cache(context: Context) {

    private val DEFAULT_SNAPSHOT_INDEX = 0

    /**
     * 磁盘缓存的默认最大大小 100MB
     * */
    private val DEFAULT_DISK_CACHE_SIZE: Long = 1024 * 1024 * 100

    /**
     * 内存缓存的默认最大大小 最大可用内存的 1/8
     * 单位为字节
     * */
    private val DEFAULT_MOMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 8 / 1024).toInt()

    val mApplicationContext = context.applicationContext

    var mLruCache = object : LruCache<String, Bitmap>(DEFAULT_MOMORY_CACHE_SIZE) {
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            return value!!.rowBytes * value.height / 1024
        }
    }

    var mDiskLruCache: DiskLruCache

    init {
        //创建缓存目录
        val file = File(mApplicationContext.externalCacheDir, "cache")
        if (!file.exists()) {
            file.mkdir()
        }
        //初始化磁盘缓存
        mDiskLruCache = DiskLruCache.open(file, 1, 1, DEFAULT_DISK_CACHE_SIZE)
    }
    
    /**
     * 将Bitmap放入缓存中
     * @param key Key
     * @param Bitmap 的输入流
     * */
    fun put(key: String, inputStream: InputStream) {
        TODO()
    }

    /**
     * 从缓存中读取Bitmap
     * @param key 缓存的Key
     * @return 返回缓存中的Bitmap，如果缓存中不存在指定的Bitmap的话，返回Null
     * */
    fun get(key: String): Bitmap? {
        var bitmap: Bitmap? = null
        //从内存中读取
        bitmap = mLruCache.get(key)
        if (bitmap == null) {
            //从磁盘中读取
            val snapshot = mDiskLruCache.get(key)
            if (snapshot != null) {
                val inputStream = snapshot.getInputStream(DEFAULT_SNAPSHOT_INDEX)
                bitmap = BitmapFactory.decodeStream(inputStream)
                //将Bitmap放入内存缓存中
                mLruCache.put(key, bitmap)
                snapshot.close()
            }
        }
        return bitmap
    }

    /**
     * 判断指定的Bitmap是否存在与磁盘缓存中
     * */
    fun isExistedInDiskCache(key: String): Boolean {
        val snapshot = mDiskLruCache.get(key)
        if (snapshot != null) {
            snapshot.close()
            return true
        }
        return false
    }

    companion object {

        private var mInstance: Cache? = null

        fun getInstance(context: Context): Cache {
            if (mInstance == null) {
                mInstance = Cache(context)
            }
            return mInstance!!
        }
    }
}