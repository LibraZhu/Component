package com.libra.utils

import android.util.Log
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by libra on 2017/8/1.
 */

object MD5 {
    private val LOG_TAG = "MD5"
    private val ALGORITHM = "MD5"

    private val sHexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    private var sDigest: MessageDigest? = null


    init {
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(LOG_TAG, "Get MD5 Digest failed.")
        }

    }


    /**
     * string md5
     */
    fun encode(source: String): String {
        val btyes = source.toByteArray()
        val encodedBytes = sDigest!!.digest(btyes)

        return hexString(encodedBytes)
    }


    /**
     * 文件 md5
     */
    fun encodeFile(inputFile: String): String? {
        // 缓冲区大小（这个可以抽出一个参数）
        val bufferSize = 256 * 1024
        var fileInputStream: FileInputStream? = null
        var digestInputStream: DigestInputStream? = null
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            var messageDigest = MessageDigest.getInstance("MD5")
            // 使用DigestInputStream
            fileInputStream = FileInputStream(inputFile)
            digestInputStream = DigestInputStream(fileInputStream, messageDigest)
            // read的过程中进行MD5处理，直到读完文件
            val buffer = ByteArray(bufferSize)
            while (digestInputStream.read(buffer) > 0) {
            }
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.messageDigest
            // 拿到结果，也是字节数组，包含16个元素
            val resultByteArray = messageDigest.digest()
            // 同样，把字节数组转换成字符串
            return hexString(resultByteArray)
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                digestInputStream!!.close()
            } catch (e: Exception) {
            }

            try {
                fileInputStream!!.close()
            } catch (e: Exception) {
            }

        }
    }


    fun hexString(source: ByteArray?): String {
        if (source == null || source.isEmpty()) {
            return ""
        }

        val size = source.size
        val str = CharArray(size * 2)
        var index = 0
        var b: Byte
        for (i in 0 until size) {
            b = source[i]
            str[index++] = sHexDigits[b.toInt().ushr(4) and 0xf]
            str[index++] = sHexDigits[b.toInt() and 0xf]
        }
        return String(str)
    }
}
