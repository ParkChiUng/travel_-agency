package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sessac.travel_agency.data.PackageItem

@Dao
interface PackageDao {
    @Query("SELECT * FROM table_package")
    fun getAll(): List<PackageItem>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<PackageItem>)
}