package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.database.AppDatabase
import java.util.Date

class GuideScheduleRepository{
    private val guideScheduleDao = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).guideScheduleDao()

    fun insertGuideSchedule(newGuideSchedule: GuideScheduleItem) {
        guideScheduleDao.insertGuideSchedule(newGuideSchedule)
    }

    fun updateGuideSchedule(updateGuideSchedule: GuideScheduleItem) {
        guideScheduleDao.updateGuideSchedule(updateGuideSchedule)
    }

    fun deleteGuideSchedule(id: Int) {
        guideScheduleDao.deleteGuideSchedule(id)
    }

    fun findGuideSchedule(startDate: Date, endDate: Date): List<GuideItem> {
        return guideScheduleDao.getGuideSchedule(startDate, endDate)
    }
}