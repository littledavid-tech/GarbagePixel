package cn.shycoder.garbagepixel.lib.utils

import java.io.InputStream
import java.io.OutputStream


fun InputStream?.safeClose() {
    try {
        this?.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun OutputStream?.safeClose() {
    try {
        this?.flush()
        this?.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

class IOUtils {

}