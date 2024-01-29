package com.sessac.travel_agency.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param guideId : Auto Increment 사용하여 자동으로 증가 예정
 * @param gName : 가이드 이름
 * @param gImage : 가이드 이미지
 */

@Entity(tableName = "table_guide")
data class GuideItem(
    @PrimaryKey(autoGenerate = true)
    val guideId : Int,
    val gName : String,
    val gImage : Int, //샘플
)
