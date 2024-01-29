package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Query
import com.sessac.travel_agency.data.ScheduleItem

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM table_schedule")
    fun getAll(): List<ScheduleItem>
}