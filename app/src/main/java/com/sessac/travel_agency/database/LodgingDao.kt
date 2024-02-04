package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sessac.travel_agency.data.LodgingItem

@Dao
interface LodgingDao {
    @Query("SELECT * FROM table_lodging")
    fun getAll(): List<LodgingItem>

    @Query("SELECT * FROM table_lodging WHERE area = :area")
    fun findLodging(area: String): List<LodgingItem>

    @Query("SELECT * FROM table_lodging WHERE lodgeId = :id")
    fun findLodgingById(id: Int): LodgingItem

    @Query("SELECT * FROM table_lodging WHERE lodgeId = :id")
    suspend fun findLodgingByIdReturn(id: Int): LodgingItem

    @Transaction
    @Insert
    fun insertLodging(lodging: LodgingItem)

    @Transaction
    @Delete
    fun deleteLodging(lodging: LodgingItem)

    @Transaction
    @Update
    fun updateLodging(lodgingItem: LodgingItem)

}