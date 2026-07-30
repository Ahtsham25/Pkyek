package com.shami.dramalib

import android.app.Application
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val file = File(filesDir, "crash.txt")
                file.writeText(sw.toString())
            } catch (e: Exception) {
                // ignore
            }
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
