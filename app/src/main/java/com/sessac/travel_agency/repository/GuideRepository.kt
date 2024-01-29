package com.sessac.travel_agency.repository

import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.database.GuideDao

class GuideRepository(private val guideDao: GuideDao) {
    val allGuide: List<GuideItem> = guideDao.getAll()
}