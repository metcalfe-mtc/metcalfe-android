package com.lang.ktn.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object FileUtils {
    fun saveBitmap(mContext: Context, bitmap: Bitmap, name: String, path: String): Boolean {
        val dirFile = File(path)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        val file = File(path, "$name.jpg")
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        try {
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(file)
        intent.data = uri
        mContext.sendBroadcast(intent)
        return true
    }
}
