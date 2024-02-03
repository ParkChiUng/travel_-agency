package com.sessac.travel_agency.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @param guideScheduleId : Auto Increment 사용하여 자동으로 증가 예정
 * @param guideId : 가이드 정보
 * @param packageId : 패키지 정보
 */
@Entity(
    tableName = "table_guide_schedule",
    foreignKeys = [
        ForeignKey(
            entity = GuideItem::class,
            parentColumns = ["guideId"],
            childColumns = ["guideId"]
        ),
        ForeignKey(
            entity = PackageItem::class,
            parentColumns = ["packageId"],
            childColumns = ["packageId"]
        )
    ]
)
data class GuideScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val guideScheduleId : Int = 0,
    val guideId : Int,
    val packageId : Int
)
