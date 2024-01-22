package com.sessac.travel_agency.data

import android.graphics.Bitmap

/**
 * @param guideId : Auto Increment 사용하여 자동으로 증가 예정
 * @param gName : 가이드 이름
 * @param gImage : 가이드 이미지
 */
data class GuideItem(
    val guideId : Int,
    val gName : String,
    val gImage : Bitmap,
)
