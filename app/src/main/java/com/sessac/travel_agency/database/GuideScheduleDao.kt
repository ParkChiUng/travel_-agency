package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Query
import com.sessac.travel_agency.data.GuideScheduleItem

@Dao
interface GuideScheduleDao {
    @Query("SELECT * FROM table_guide_schedule")
    fun getAll(): List<GuideScheduleItem>
}