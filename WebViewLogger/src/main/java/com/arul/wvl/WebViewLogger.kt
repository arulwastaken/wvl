package com.arul.wvl

import android.app.Application
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.arul.wvl.bridge.WebViewEvent

open class WebViewLogger(val application: Application) : WebChromeClient() {

    val defaultSection = "WVL_SECTION"

    private val event = WebViewEvent.getInstance(application)

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d("WebViewLogger", "Logger onConsoleMessage() called with: consoleMessage = $consoleMessage")
        onConsoleMessage(consoleMessage, defaultSection)
        return true
    }

    open fun onConsoleMessage(consoleMessage: ConsoleMessage?, section: String = "WVL_DEFAULT_SECTION") {
        Log.d(
            "WebViewLogger",
            "Logger onConsoleMessage() called with: consoleMessage = $consoleMessage, section = $section end"
        )
        consoleMessage?.let {
            event.log(it, section)
        }
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
    }


}