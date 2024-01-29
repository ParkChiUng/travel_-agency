package com.sessac.travel_agency.repository

import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.database.GuideScheduleDao

class GuideScheduleRepository(private val guideScheduleDao: GuideScheduleDao) {
    val allGuideSchedule: List<GuideScheduleItem> = guideScheduleDao.getAll()
}