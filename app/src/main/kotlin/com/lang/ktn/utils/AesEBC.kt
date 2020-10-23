package com.lang.ktn.utils

import java.nio.charset.Charset
import java.util.Arrays

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by ziyong on 2018/12/13.
 * com.bvc.adt.utils.AesEBC
 * —。—
 */
object AesEBC {

    //加密
    @Throws(Exception::class)
    fun encrypt(pwd: String, content: String): String {
        val key = SecretKeySpec(Arrays.copyOf(pwd.toByteArray(charset("utf-8")), 16), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val byteContent = content.toByteArray(charset("utf-8"))
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val result = cipher.doFinal(byteContent)
        return Base64Util.encode(result)
    }

    //解密
    @Throws(Exception::class)
    fun ecrypt(pwd: String, contentEncrypt: String): String {

        val raw = Arrays.copyOf(pwd.toByteArray(charset("utf-8")), 16)
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        val encrypted1 = Base64Util.decode(contentEncrypt)//先用base64解密

        val original = cipher.doFinal(encrypted1)
        return String(original, Charset.forName("utf-8"))
    }


}
