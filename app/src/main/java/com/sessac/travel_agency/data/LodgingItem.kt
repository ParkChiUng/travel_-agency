package com.sessac.travel_agency.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @param lodgeId : Auto Increment 사용하여 자동으로 증가 예정
 * @param area : 지역명
 * @param lName : 숙소명
 * @param lImage : 숙소 이미지
 * @param starNum : 숙소 등급
 */

@Entity(tableName = "table_lodging")
data class LodgingItem(
    @PrimaryKey(autoGenerate = true)
    val lodgeId : Int = 0,
    val area : String,
    val lName : String,
    val lImage : String,
    val starNum : Int
)
