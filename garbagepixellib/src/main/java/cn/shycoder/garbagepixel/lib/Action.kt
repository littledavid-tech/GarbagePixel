package cn.shycoder.garbagepixel.lib

import android.widget.ImageView
import java.lang.ref.WeakReference

class Action(val source: Any, val weakReference: WeakReference<ImageView>) {

    /**
     *
     * */
    internal class ActionBuilder {

        fun into(imageView: ImageView) {

        }
    }
}