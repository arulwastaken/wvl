package com.arul.wvl.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogDao {
    @Query("SELECT * FROM WvlLog")
    fun getAll(): List<WvlLog>

    @Query("SELECT * FROM WvlLog WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<WvlLog>

    @Query("SELECT * FROM WvlLog WHERE section IN (:section)")
    fun getLogBySection(section: String): List<WvlLog>

//    @Query("SELECT * FROM WvlLog WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): WvlLog

    @Insert
    fun insertAll(users: List<WvlLog>)

    @Delete
    fun delete(user: WvlLog)

    @Query("DELETE FROM WvlLog")
    fun deleteAll()
}