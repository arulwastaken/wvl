package com.arul.wvl

import com.arul.wvl.datasource.WvlLog

internal interface WebViewLogListener {
    fun onAppendLog(consoleLog: WvlLog)
}