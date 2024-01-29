package com.sessac.travel_agency.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param guideScheduleId : Auto Increment 사용하여 자동으로 증가 예정
 * @param guideInfo : 가이드 정보
 * @param packageInfo : 패키지 정보
 */
@Entity(tableName = "table_guide_schedule")
data class GuideScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val guideScheduleId : Int,
    val guideInfo : Int,
    val packageInfo : Int
)
