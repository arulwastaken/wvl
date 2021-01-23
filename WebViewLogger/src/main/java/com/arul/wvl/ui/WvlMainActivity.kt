package com.arul.wvl.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arul.wvl.R
import com.arul.wvl.WebViewLogListener
import com.arul.wvl.WvlLogAdapter
import com.arul.wvl.bridge.RxBus
import com.arul.wvl.bridge.WebViewEvent
import com.arul.wvl.datasource.RxEvent
import com.arul.wvl.datasource.WvlLog
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.wvl_activity_main.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

@SuppressLint("SetTextI18n")
@ExperimentalStdlibApi
internal class WvlMainActivity : AppCompatActivity(), WebViewLogListener, CoroutineScope {

    private lateinit var eventBus: Disposable

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: WvlLogAdapter

    private var canShowLive: Boolean = true

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wvl_activity_main)
        setSupportActionBar(wvl_toolbar)
        setupLogger()
        setupRecycler()
        doOperation()
    }

    private fun doOperation() {
        mvl_move_to_last.setOnClickListener {
            if (adapter.itemCount != 0) {
                wvl_log_rv.scrollToPosition(adapter.itemCount - 1)
                canShowLive = true
            }
        }

        mvl_move_to_first.setOnClickListener {
            canShowLive = false
            wvl_log_rv.scrollToPosition(0)
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (canShowLive())
                    wvl_log_rv.scrollToPosition(adapter.itemCount - 1)
            }
        })

        wvl_log_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    canShowLive = false
                }
            }
        })
    }

    fun canShowLive() = canShowLive

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.wvl_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.send_report -> {
                sendReport()
                true
            }
            R.id.clear_log -> {
                clearLog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearLog() {
        launch(Dispatchers.IO) {
            WebViewEvent.getInstance(application).db.logDao().deleteAll()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@WvlMainActivity, "Log Cleared", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendReport() {
        val job = launch(Dispatchers.IO) {
            val mailTo = getString(R.string.wvl_report_email)
            val allLogList = WebViewEvent.getInstance(application).db.logDao().getAll()
            val allLogText = Gson().toJson(allLogList)
            println("All Logs printes $allLogText")
            val file = File(getExternalFilesDir(null)?.absolutePath + "/temp.log")
            try {
                val fileWriter = FileWriter(file)
                fileWriter.append(allLogText)
                fileWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val uri = FileProvider.getUriForFile(this@WvlMainActivity, "com.arul.wvl.report.provider", file)
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "vnd.android.cursor.dir/email"
            val to = arrayOf(mailTo)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Web Log")
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }
    }

    private fun setupRecycler() {
        linearLayoutManager = LinearLayoutManager(this)
        adapter = WvlLogAdapter(ArrayList(), this)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        wvl_log_rv.addItemDecoration(dividerItemDecoration)
        wvl_log_rv.adapter = adapter
        wvl_log_rv.layoutManager = linearLayoutManager
    }

    @ExperimentalStdlibApi
    private fun setupLogger() {
        eventBus = RxBus.listen(RxEvent.EventAddLog::class.java).subscribe {
            if (adapter.itemCount > 200) {
                adapter.removeOldLog()
            }
            adapter.updateLog(it.eventLog)
        }
    }

    override fun onAppendLog(consoleLog: WvlLog) {
        Log.d(TAG, "onAppendLog() called with: consoleLog = $consoleLog")
        if (adapter.itemCount > 200) {
            adapter.removeOldLog()
        }
        adapter.updateLog(consoleLog)
    }

    override fun onDestroy() {
        try { eventBus.dispose() } catch (e: Exception) {}
        super.onDestroy()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}
