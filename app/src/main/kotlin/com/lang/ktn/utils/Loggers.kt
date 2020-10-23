package com.lang.ktn.utils

import android.os.Environment
import android.util.Log

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object Loggers {


    private val loggable = true//BuildConfig.DEBUG;//BuildConfig.DEBUG;
    private val tag = "loggable"
    private val logfilename = "loggable_File.txt"
    private val recordable = false

    /**
     *
     * @params lvl
     * @params action
     */
    fun record(s: String) {
        if (!recordable)
            return
        try {
            val root: File
            if (loggable) {
                root = Environment.getExternalStorageDirectory()
            } else {
                root = File("/")
            }
            if (root.canWrite()) {
                val gpxfile = File(root, logfilename)
                if (!gpxfile.exists())
                    gpxfile.createNewFile()
                val gpxwriter = FileWriter(gpxfile, true)
                val out = BufferedWriter(gpxwriter)
                out.write(s + "\n")
                out.close()
            }
        } catch (e: IOException) {
            Loggers.e("Could not write file " + e.message)
        }

    }

    fun e(msg: String) {
        if (loggable) {
            Log.e(tag, msg)
            record(msg)
        }
    }

    fun i(msg: String) {
        if (loggable) {
            Log.i(tag, msg)
            record(msg)
        }
    }

}
