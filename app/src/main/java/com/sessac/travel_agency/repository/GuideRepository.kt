package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.database.AppDatabase

class GuideRepository {

    private val guideDao =
        AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).guideDao()

    fun insertGuide(newGuide: GuideItem) {
        guideDao.insertGuide(newGuide)
    }

    fun updateGuide(updateGuide: GuideItem) {
        guideDao.updateGuide(updateGuide)
    }

    fun deleteGuide(id: Int) {
        guideDao.deleteGuide(id)
    }

    fun findAllProducts(): List<GuideItem> {
        return guideDao.getAllGuideList()
    }
}