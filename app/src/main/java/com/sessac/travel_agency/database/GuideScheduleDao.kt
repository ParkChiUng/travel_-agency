package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import java.util.Date

@Dao
interface GuideScheduleDao {
    @Query("SELECT * FROM table_guide WHERE guideId NOT IN " +
            "(SELECT guideId FROM table_guide_schedule WHERE packageId IN " +
            "(SELECT packageId FROM table_package WHERE (pstartDate <= :endDate) " +
            "AND (pendDate >= :startDate)))")
    fun getGuideSchedule(startDate: Date, endDate: Date): List<GuideItem>

    @Transaction
    @Insert
    fun insertGuideSchedule(guideScheduleItem: GuideScheduleItem)

    @Transaction
    @Query("DELETE FROM table_guide_schedule WHERE guideScheduleId = :guideScheduleId")
    fun deleteGuideSchedule(guideScheduleId: Int)

    @Transaction
    @Update
    fun updateGuideSchedule(guideScheduleItem: GuideScheduleItem)
}