package cn.shycoder.garbagepixel.lib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by ITSoftware on 10/29/2018.
 */

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

object Utils {
    fun decodeMD5(str: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(str.toByteArray())
        return BigInteger(1, messageDigest.digest()).toString(16)
    }

    @SuppressLint("MissingPermission")
    fun networkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null
    }
}