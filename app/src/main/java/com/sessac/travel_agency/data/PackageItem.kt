package com.sessac.travel_agency.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * @param packageId : Auto Increment 사용하여 자동으로 증가
 * @param guideName : 가이드 이름
 * @param area : 지역명
 * @param pName : 패키지 명
 * @param pImage : 패키지 이미지
 * @param pStartDate : 패키지 시작 날짜
 * @param pEndDate : 패키지 종료 날짜
 */
@Parcelize
@Entity(tableName = "table_package")
data class PackageItem(
    @PrimaryKey(autoGenerate = true)
    val packageId : Int = 0,
    val guideName : String,
    val area : String,
    val pName : String,
    val pImage : String,
    val pStartDate : Date,
    val pEndDate : Date,
    val star : Float? = null
) : Parcelable
