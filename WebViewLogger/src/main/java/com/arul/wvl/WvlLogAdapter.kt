package com.arul.wvl

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arul.wvl.datasource.WvlLog
import com.arul.wvl.utils.getFormatDate
import kotlin.collections.ArrayList

class WvlLogAdapter(val logs: ArrayList<WvlLog>, val context: Context): RecyclerView.Adapter<WvlLogAdapter.WvlViewHolder>() {


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WvlViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wvl_log, null)
        return WvlViewHolder(view)
    }

    override fun onBindViewHolder(holder: WvlViewHolder, position: Int) {
        val log =  logs.get(position)
        holder.bindLog(log, position)
    }

    override fun getItemCount(): Int = logs.size

    fun updateLog(consoleMessage: WvlLog) {
        logs.add(consoleMessage)
        notifyDataSetChanged()
    }

    @ExperimentalStdlibApi
    fun removeOldLog() {
        logs.removeFirst()
    }

    class WvlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var time : TextView? = null
        var log : TextView? = null
        private var logLine: TextView? =null

        private var logSection: RelativeLayout? = null

        init {
            time = itemView.findViewById(R.id.it_wvl_time)
            log = itemView.findViewById(R.id.it_wvl_log)
            logLine = itemView.findViewById(R.id.it_wvl_line)
            logSection = itemView.findViewById(R.id.log_section)
        }

        fun bindLog(consoleLog: WvlLog, position: Int) {
            time?.text = consoleLog.time.getFormatDate()
            log?.text = consoleLog.message
            logLine?.text = "L: ${consoleLog.lineNumber}"

            if(position % 2 == 0) {
                logSection?.setBackgroundColor(itemView.context.resources.getColor(R.color.light_periwinkle))
            } else {
                logSection?.setBackgroundColor(Color.WHITE)
            }


            var colorByLog: Int = -1

            when(consoleLog.logType) {
                ConsoleMessage.MessageLevel.WARNING -> {
                    colorByLog = R.color.yellow_light
                }
                ConsoleMessage.MessageLevel.ERROR -> {
                    colorByLog = R.color.red_light
                }
                ConsoleMessage.MessageLevel.DEBUG -> {
                    colorByLog = R.color.blue_light
                }
            }

            if(colorByLog != -1) {
                logSection?.setBackgroundColor(itemView.context.resources.getColor(colorByLog))
            }

        }
    }
}