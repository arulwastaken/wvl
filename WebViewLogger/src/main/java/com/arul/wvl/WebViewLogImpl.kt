package com.arul.wvl

import com.arul.wvl.datasource.WvlLog

interface WebViewLogImpl {
    fun onAppendLog(consoleLog: WvlLog)
}