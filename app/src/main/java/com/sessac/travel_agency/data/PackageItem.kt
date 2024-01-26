package com.sessac.travel_agency.data

import android.graphics.Bitmap
import java.util.Date

/**
 * @param packageId : Auto Increment 사용하여 자동으로 증가 예정
 * @param guideInfo : 가이드 정보
 * @param area : 지역명
 * @param pName : 패키지 명
 * @param pImage : 패키지 이미지
 * @param status : 패키지 상태 / 0 == 종료, 1 == 진행중, 2 == 완료
 * @param pStartDate : 패키지 시작 날짜
 * @param pEndDate : 패키지 종료 날짜
 *
 */
data class PackageItem(
    val packageId : Int,
    val guideInfo : GuideItem,
    val area : String,
    val pName : String,
    val pImage : Int, // 샘플
    val status : Int,
    val pStartDate : Date,
    val pEndDate : Date,
    val star : Double? = null // 종료된 패키지 별점
)
