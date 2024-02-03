package com.sessac.travel_agency.repository

import androidx.room.Query
import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.database.AppDatabase
import java.util.Date

class PackageRepository{

    private val packageDao = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).packageDao()

    fun insertPackage(newPackage: PackageItem) {
        packageDao.insertPackage(newPackage)
    }

    fun updatePackage(updatePackage: PackageItem) {
        packageDao.updatePackage(updatePackage)
    }

    fun deletePackage(id: Int) {
        packageDao.deletePackage(id)
    }

    fun getPastPackages(currentDate: Date): List<PackageItem>{
        return packageDao.getPastPackages(currentDate)
    }

    fun getCurrentPackages(currentDate: Date): List<PackageItem>{
        return packageDao.getCurrentPackages(currentDate)
    }

    fun getFuturePackages(currentDate: Date): List<PackageItem>{
        return packageDao.getFuturePackages(currentDate)
    }

//    fun findPackage(status: Int): List<PackageItem> {
//        return packageDao.getAllPackageList(status)
//    }
}