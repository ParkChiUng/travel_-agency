package com.sessac.travel_agency.data

/**
 * @param guideScheduleId : Auto Increment 사용하여 자동으로 증가 예정
 * @param guideInfo : 가이드 정보
 * @param packageInfo : 패키지 정보
 */
data class GuideScheduleItem(
    val guideScheduleId : Int,
    val guideInfo : GuideItem,
    val packageInfo : PackageItem
)
