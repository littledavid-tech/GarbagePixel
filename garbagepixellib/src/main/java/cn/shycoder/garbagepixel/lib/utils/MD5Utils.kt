package cn.shycoder.garbagepixel.lib.utils

import java.math.BigInteger
import java.security.MessageDigest

internal object MD5Utils {
    fun decodeMD5(str: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(str.toByteArray())
        return BigInteger(1, messageDigest.digest()).toString(16)
    }
}