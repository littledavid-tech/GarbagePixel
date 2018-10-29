package cn.shycoder.garbagepixel.lib

import android.widget.ImageView
import java.lang.ref.WeakReference

class Action(val source: Any, target: ImageView, val request: Request) {
    val target: WeakReference<ImageView> = WeakReference(target)
}