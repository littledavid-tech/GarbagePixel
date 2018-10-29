package cn.shycoder.garbagepixel.lib

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by ShyCoder on 10/28/2018.
 */
internal class Despatcher(val context: Context,
                          val executor: ThreadPoolExecutor,
                          val mainHandler: Handler,
                          val cache: Cache) {


    private lateinit var despatcherThread: DespatcherThread
//    private lateinit var

    fun despatchSubmit(action: Action) {

    }

    fun despatchCompelelate() {

    }

    class DespatcherThread : HandlerThread("DespatcherThread") {

    }

    class DespatcherHandler(looper: Looper, val despatcher: Despatcher) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
}