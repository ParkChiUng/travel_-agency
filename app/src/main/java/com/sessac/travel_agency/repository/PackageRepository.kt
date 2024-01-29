package com.sessac.travel_agency.repository

import android.content.Context
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.database.AppDatabase
import com.sessac.travel_agency.database.PackageDao

//class PackageRepository(private val packageDao: PackageDao) {
class PackageRepository(context: Context) {

    private val packageDao = AppDatabase.getDatabase(context).packageDao()

    suspend fun getPackageList() = packageDao.getAll()

//    suspend fun setPackageList() = packageDao.insertAll()
}