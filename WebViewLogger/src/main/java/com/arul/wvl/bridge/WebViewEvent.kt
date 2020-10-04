package com.arul.wvl.bridge

import android.app.Application
import android.content.Context
import android.content.Intent
import android.webkit.ConsoleMessage
import androidx.room.Room
import com.arul.wvl.WebViewLogListener
import com.arul.wvl.db.LogDb
import com.arul.wvl.ui.WvlMainActivity
import com.arul.wvl.utils.SingletonHolder
import com.arul.wvl.utils.getLog
import com.arul.wvl.utils.toDbLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class WebViewEvent(val applicationContext: Context) {

    lateinit var db : LogDb

    init {
        setupDb()
    }

    private fun setupDb() {
        db = Room.databaseBuilder(
                applicationContext,
                LogDb::class.java, "database-name"
        ).build()
    }

    @Volatile
    private var listener: WebViewLogListener? = null

    fun log(consoleMessage: ConsoleMessage, sectionName: String) {
        val consoleLog = consoleMessage.getLog(sectionName)
        GlobalScope.launch {
            db.logDao().insertAll(consoleLog.toDbLog())
        }
        listener?.onAppendLog(consoleLog)
    }

    fun setSection(sectionName: String) {

    }

    fun <T : Context, R : Class<R>> launchWebviewLogger(context: T, source: Class<R>) {
        context.startActivity(Intent(context, WvlMainActivity::class.java))
    }

    fun bindListener(listener: WebViewLogListener?) {
        this.listener = listener
    }

    companion object : SingletonHolder<WebViewEvent, Application>(::WebViewEvent)
}