package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sessac.travel_agency.data.GuideItem

@Dao
interface GuideDao {
    @Query("SELECT * FROM table_guide")
    fun getAllGuideList(): List<GuideItem>

    @Transaction
    @Insert
    fun insertGuide(guideItem: GuideItem)

    @Transaction
    @Query("DELETE FROM table_guide WHERE guideId = :guideId")
    fun deleteGuide(guideId: Int)

    @Transaction
    @Update
    fun updateGuide(guideItem: GuideItem)
}