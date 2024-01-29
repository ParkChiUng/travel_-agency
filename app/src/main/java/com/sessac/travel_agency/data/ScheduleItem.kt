package com.sessac.travel_agency.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param scheduleId : Auto Increment 사용하여 자동으로 증가 예정
 * @param lodgingInfo : 숙소 정보
 * @param theme : 테마명
 * @param detail : 일정 상세 설명
 * @param day : 일차 ex) day == 1 -> 1일차
 */

@Entity(tableName = "table_schedule")
data class ScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val scheduleId : Int,
    val lodgingInfo : Int,
    val theme : String,
    val detail : String,
    val day : Int
)
