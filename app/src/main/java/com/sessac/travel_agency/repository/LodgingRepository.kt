package com.sessac.travel_agency.repository

import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.database.LodgingDao

class LodgingRepository(private val lodgingDao: LodgingDao) {
    val allLodging: List<LodgingItem> = lodgingDao.getAll()
}