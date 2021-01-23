package com.arul.wvl.datasource

import android.webkit.ConsoleMessage
import java.util.*

data class WvlLog(val time: Date, val lineNumber: Int, val message: String, val logType: ConsoleMessage.MessageLevel = ConsoleMessage.MessageLevel.TIP, val section: String? = null)
