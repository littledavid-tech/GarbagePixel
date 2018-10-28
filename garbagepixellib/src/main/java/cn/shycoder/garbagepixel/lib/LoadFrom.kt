package cn.shycoder.garbagepixel.lib

/**
 * Created by ShyCoder on 10/28/2018.
 */

/**
 * Bitmap的来源
 * */
enum class LoadFrom {
    /**
     * 直接从来源下载/加载,不考虑缓存
     * */
    SOURCE,
    /**
     * 优先从缓存中加载，如果缓存中不存在，再从网络中下载
     * */
    ANY
}