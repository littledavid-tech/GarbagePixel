package cn.shycoder.garbagepixel.lib

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import java.util.concurrent.ThreadPoolExecutor


/**
 * 任务的调度者
 * 负责开启线程执行任务并且处理结果
 * */
class Dispatcher(
        private val pixel: Pixel,
        private val context: Context,
        private val executor: ThreadPoolExecutor,
        private val mainHandler: Handler,
        private val cache: Cache) {

    private val TAG = Dispatcher::class.java.name

    private val mDispatcherThread: DispatcherThread

    private val mHandler: DispatcherHandler

    init {
        mDispatcherThread = DispatcherThread()
        mDispatcherThread.start()
        mHandler = DispatcherHandler(mDispatcherThread.looper, this)
    }

    fun dispatchSubmit(action: Action) {
        if (pixel.isDebugging()) {
            Log.e(TAG, "Submit task， Key:${action.request.bitmapKey.processedKey}")
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUBMIT, action))
    }

    fun dispatchComplete(bitmapProcessor: BitmapProcessor) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_COMPELETE, bitmapProcessor))
    }

    /**
     * 任务失败-发送任务失败的消息
     * @param action Action
     * @param exception 异常对象
     * */
    fun dispatchFailed(bitmapProcessor: BitmapProcessor) {
        if (pixel.isDebugging()) {
            Log.e(TAG, "Task failed， Key:${bitmapProcessor.request.bitmapKey.processedKey}")
        }
    }

    fun performSubmit(action: Action) {
        //设置占位符
        if (action.request.placeholderDrawable != null) {
            mainHandler.post {
                if (action.target.get() != null) {
                    action.target.get()!!.setImageDrawable(action.request.placeholderDrawable)
                    //如果是一个帧动画的话，开启帧动画
                    if (action.request.placeholderDrawable is AnimationDrawable) {
                        action.request.placeholderDrawable.start()
                    }
                }
            }
        }

        executor.submit(BitmapProcessor.create(pixel, this, action))
    }

    fun performComplete(bitmapProcessor: BitmapProcessor) {
        mainHandler.sendMessage(mainHandler.obtainMessage(Pixel.MSG_ACTION_COMPELETE, bitmapProcessor))
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
                    dispatcher.performSubmit(msg.obj as Action)
                }
                MSG_TASK_COMPELETE -> {
                    dispatcher.performComplete(msg.obj as BitmapProcessor)
                }
                MSG_TASK_FAILED -> {

                }
                else -> {

                }
            }
        }
    }
}