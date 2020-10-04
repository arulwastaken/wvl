package com.arul.wvl.utils

import android.webkit.ConsoleMessage
import com.arul.wvl.datasource.WvlLog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


const val date_format = "HH:mm:ss"

var parser = SimpleDateFormat(date_format,Locale.getDefault())

val PARSE_DATE_ISO: DateFormat by lazy {
    val ti = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    ti.timeZone = TimeZone.getTimeZone("UTC")
    return@lazy ti
}

fun Date.convertToIso(): String {
    return PARSE_DATE_ISO.format(this)
}

fun ConsoleMessage.getLog(section: String? = null): WvlLog {
    return WvlLog(Date(), lineNumber(), message(), messageLevel(), section)
}

fun WvlLog.toDbLog(): com.arul.wvl.db.WvlLog {
    return com.arul.wvl.db.WvlLog(time.convertToIso(),message, logType.name, section, lineNumber)
}

fun Date.getFormatDate(): String = parser.format(this)