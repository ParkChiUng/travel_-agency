package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sessac.travel_agency.data.ScheduleItem

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM table_schedule WHERE packageId = :packageId")
    fun getScheduleList(packageId: Int): List<ScheduleItem>

    @Transaction
    @Insert
    fun insertSchedule(scheduleItem: ScheduleItem)

    @Transaction
    @Query("DELETE FROM table_schedule WHERE scheduleId = :scheduleId")
    fun deleteSchedule(scheduleId: Int)

    @Transaction
    @Update
    fun updateSchedule(scheduleItem: ScheduleItem)
}