package com.sessac.travel_agency.repository

import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.database.ScheduleDao

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    val allSchedules: List<ScheduleItem> = scheduleDao.getAll()
}