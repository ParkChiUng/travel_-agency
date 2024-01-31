package com.sessac.travel_agency.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sessac.travel_agency.data.LodgingItem

@Dao
interface LodgingDao {
    // 모든 리스트 가져옴
    @Query("SELECT * FROM table_lodging")
    fun getAll(): List<LodgingItem>

    // 지역명으로 모든 리스트 가져옴

    @Query("SELECT * FROM table_lodging WHERE area = :area")
    fun findLodging(area: String): List<LodgingItem>

    // 삽입
    @Insert
    fun insertLodging(lodging: LodgingItem)

    // 삭제
    @Delete
    fun deleteLodging(lodging: LodgingItem)

    // 수정. 사진/숙소명/지역/등급

}