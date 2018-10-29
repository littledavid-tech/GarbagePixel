package cn.shycoder.garbagepixel.lib

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import java.util.concurrent.ThreadPoolExecutor


/**
 * 任务的调度者
 * 负责开启线程执行任务并且处理结果
 * */
internal class Dispatcher(
        private val context: Context,
        private val executor: ThreadPoolExecutor,
        private val mainHandler: Handler,
        private val cache: Cache) {

    private val mDispatcherThread: DispatcherThread

    private val mHandler: DispatcherHandler

    init {
        mDispatcherThread = DispatcherThread()
        mDispatcherThread.start()
        mHandler = DispatcherHandler(mDispatcherThread.looper, this)
    }

    fun dispatchSubmit(action: Action) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUBMIT, action))
    }

    fun dispatchCompelelate(action: Action) {

    }

    /**
     * 任务失败-发送任务失败的消息
     * @param action Action
     * @param exception 异常对象
     * */
    fun dispatchFailed(action: Action) {

    }

    companion object {
        private val MSG_TASK_SUBMIT = 0
        private val MSG_TASK_COMPELETE = 1
        private val MSG_TASK_FAILED = 2
    }

    class DispatcherThread : HandlerThread("DispatcherThread") {

    }

    class DispatcherHandler(looper: Looper, val dispatcher: Dispatcher) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                MSG_TASK_SUBMIT -> {

                }
                MSG_TASK_COMPELETE -> {

                }
                MSG_TASK_FAILED -> {

                }
                else -> {

                }
            }
        }
    }
}