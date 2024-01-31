package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.database.AppDatabase
import com.sessac.travel_agency.database.LodgingDao

class LodgingRepository() {
    //class LodgingRepository(private val lodgingDao: LodgingDao) {
    private var lodgingDao: LodgingDao
    init {
        val travelDB = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication())
        lodgingDao = travelDB.lodgingDao()
    }

    val allLodging: List<LodgingItem> = lodgingDao.getAll()

    fun findAllLodging() : List<LodgingItem> {
        return lodgingDao.getAll()
    }

    fun findLodging(area: String) : List<LodgingItem> {
        return lodgingDao.findLodging(area)
    }
    fun insertLodging(lodging: LodgingItem) {
        lodgingDao.insertLodging(lodging)
    }

    fun deleteLodging(lodging: LodgingItem) {
        lodgingDao.deleteLodging(lodging)
    }

}