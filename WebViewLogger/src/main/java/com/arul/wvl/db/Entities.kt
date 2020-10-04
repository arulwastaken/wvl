package com.arul.wvl.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WvlLog(
        @ColumnInfo(name = "time") val time: String,
        @ColumnInfo(name = "message") val message: String?,
        @ColumnInfo(name = "log_type") val logType: String?,
        @ColumnInfo(name = "section") val section: String?,
        @ColumnInfo(name = "log_line") val logLine: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}