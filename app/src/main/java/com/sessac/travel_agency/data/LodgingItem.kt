package com.sessac.travel_agency.data

import android.graphics.Bitmap
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize

/**
 * @param lodgeId : Auto Increment 사용하여 자동으로 증가 예정
 * @param area : 지역명
 * @param lName : 숙소명
 * @param lImage : 숙소 이미지
 * @param starNum : 숙소 등급
 */
//@Parcelize //gradle 플러그인에 parcelize 추가함. 데이터 넘기기 위해
data class LodgingItem(
    val lodgeId : Int,
    val area : String,
    val lName : String,
    val lImage : Int, // 샘플 Bitmap파일이 Int로 인식되어 잠시 Int로
    val starNum : Int
) //: Parcelable
