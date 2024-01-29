package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Query
import com.sessac.travel_agency.data.LodgingItem

@Dao
interface LodgingDao {
    @Query("SELECT * FROM table_lodging")
    fun getAll(): List<LodgingItem>
}