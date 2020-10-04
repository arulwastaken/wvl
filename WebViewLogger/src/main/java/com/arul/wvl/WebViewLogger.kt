package com.arul.wvl

import android.app.Application
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import com.arul.wvl.bridge.WebViewEvent

abstract class WebViewLogger(val application: Application) : WebChromeClient() {

    private var event = WebViewEvent.getInstance(application)

    abstract fun getSectionName(): String

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d("WebViewLogger", "onConsoleMessage() called with: consoleMessage = $consoleMessage")
        consoleMessage?.let {
            event.log(it, getSectionName())
        }
        return super.onConsoleMessage(consoleMessage)
    }

    fun setSection(sectionName: String) {
        event.setSection(sectionName)
    }

}