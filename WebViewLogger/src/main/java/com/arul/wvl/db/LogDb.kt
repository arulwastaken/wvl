package com.arul.wvl.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WvlLog::class], version = 1, exportSchema = false)
abstract class LogDb : RoomDatabase() {
    abstract fun logDao(): LogDao
}