package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.database.AppDatabase
import com.sessac.travel_agency.database.LodgingDao

/**
 * RoomDB에 액세스하여 LodgingItem을 CRUD, 모든 LodgingItem을 가져옴
 * */
class LodgingRepository {

    private val lodgingDao = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).lodgingDao()

    fun insertLodging(newLodging: LodgingItem) {
        lodgingDao.insertLodging(newLodging)
    }

    fun updateLodging(updateLodging: LodgingItem) {
        lodgingDao.updateLodging(updateLodging)
    }

    fun deleteLodging(deleteLodging: LodgingItem) {
        lodgingDao.deleteLodging(deleteLodging)
    }

    fun findAllLodgings(): List<LodgingItem> {
        return lodgingDao.getAll()
    }

    fun findLodgingsByArea(area: String): List<LodgingItem> {
        return lodgingDao.findLodging(area)
    }
}