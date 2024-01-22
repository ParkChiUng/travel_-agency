package com.sessac.travel_agency.data

import android.graphics.Bitmap

/**
 * @param lodgeId : Auto Increment 사용하여 자동으로 증가 예정
 * @param area : 지역명
 * @param lName : 숙소명
 * @param lImage : 숙소 이미지
 * @param starNum : 숙소 등급
 */
data class LodgingItem(
    val lodgeId : Int,
    val area : String,
    val lName : String,
    val lImage : Bitmap,
    val starNum : Int
)
