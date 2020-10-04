package com.arul.wvl

import com.arul.wvl.datasource.WvlLog

interface WebViewLogListener {
    fun onAppendLog(consoleLog: WvlLog)
}