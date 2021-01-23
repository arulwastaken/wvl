package com.arul.wvl.bridge

import android.app.Application
import android.content.Context
import android.webkit.ConsoleMessage
import androidx.room.Room
import com.arul.wvl.R
import com.arul.wvl.datasource.RxEvent
import com.arul.wvl.db.LogDb
import com.arul.wvl.db.WvlLog
import com.arul.wvl.utils.SingletonHolder
import com.arul.wvl.utils.getLog
import com.arul.wvl.utils.toDbLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class WebViewEvent(private val applicationContext: Context) {

    lateinit var db: LogDb

    init {
        setupDb()
    }

    private fun setupDb() {
        db = Room.databaseBuilder(
            applicationContext,
            LogDb::class.java, "wvl-database"
        ).build()
    }

    private var cacheLog = ArrayList<WvlLog>()
    private val cacheSize by lazy { applicationContext.getString(R.string.wvl_log_cache_size).toInt() ?: 10 }

    fun log(consoleMessage: ConsoleMessage, sectionName: String) {
        cacheLog.add(consoleMessage.getLog(sectionName).toDbLog())
        RxBus.publish(RxEvent.EventAddLog(consoleMessage.getLog(sectionName)))

        if (cacheSize < cacheLog.size) {
            GlobalScope.launch {
                db.logDao().insertAll(cacheLog)
            }
            cacheLog.clear()
        }
    }

    companion object : SingletonHolder<WebViewEvent, Application>(::WebViewEvent)
}
