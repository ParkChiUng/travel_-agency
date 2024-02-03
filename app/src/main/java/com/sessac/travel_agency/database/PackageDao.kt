package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sessac.travel_agency.data.PackageItem
import java.util.Date

@Dao
interface PackageDao {
//    @Query("SELECT * FROM table_package WHERE status = :status")
//    fun getAllPackageList(status: Int): List<PackageItem>

    @Query("SELECT * FROM table_package WHERE pEndDate < :currentDate")
    fun getPastPackages(currentDate: Date): List<PackageItem>

    @Query("SELECT * FROM table_package WHERE pStartDate <= :currentDate AND pEndDate >= :currentDate")
    fun getCurrentPackages(currentDate: Date): List<PackageItem>

    @Query("SELECT * FROM table_package WHERE pStartDate > :currentDate")
    fun getFuturePackages(currentDate: Date): List<PackageItem>

    @Transaction
    @Insert
    fun insertPackage(packageItem: PackageItem)

    @Transaction
    @Query("DELETE FROM table_package WHERE packageId = :packageId")
    fun deletePackage(packageId: Int)

    @Transaction
    @Update
    fun updatePackage(packageItem: PackageItem)
}