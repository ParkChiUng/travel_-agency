package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.database.AppDatabase

class ScheduleRepository {
    private val scheduleDao = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).scheduleDao()

    fun insertSchedule(newSchedule: ScheduleItem) {
        scheduleDao.insertSchedule(newSchedule)
    }

    fun updateSchedule(updateSchedule: ScheduleItem) {
        scheduleDao.updateSchedule(updateSchedule)
    }

    fun deleteSchedule(id: Int) {
        scheduleDao.deleteSchedule(id)
    }

    fun findSchedules(packageId: Int): List<ScheduleItem> {
        return scheduleDao.getScheduleList(packageId)
    }
}