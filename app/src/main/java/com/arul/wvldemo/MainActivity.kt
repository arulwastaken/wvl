package com.arul.wvldemo

import android.os.Bundle
import android.webkit.ConsoleMessage
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.arul.wvl.WebViewLogger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebView.setWebContentsDebuggingEnabled(true)
        webview.settings.apply {
            javaScriptEnabled = true
        }
        webview.webChromeClient = object : WebViewLogger(application = application) {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?, section: String) {
                super.onConsoleMessage(consoleMessage, section)
            }
        }
        webview.loadUrl("https://dev.to")
    }
}
