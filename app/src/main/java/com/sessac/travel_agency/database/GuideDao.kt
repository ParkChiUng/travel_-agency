package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Query
import com.sessac.travel_agency.data.GuideItem

@Dao
interface GuideDao {
    @Query("SELECT * FROM table_guide")
    fun getAll(): List<GuideItem>
}