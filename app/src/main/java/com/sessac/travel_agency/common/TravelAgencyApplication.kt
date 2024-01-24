package com.sessac.travel_agency.common

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.sessac.travel_agency.helper.TravelAgencyOpenHelper

class TravelAgencyApplication : Application() {
    lateinit var dbHelper: TravelAgencyOpenHelper
    lateinit var db: SQLiteDatabase

    override fun onCreate() {
        super.onCreate()
        dbHelper = TravelAgencyOpenHelper(applicationContext)
        db = dbHelper.writableDatabase
        travelApplication = this
    }

    companion object {
        private lateinit var travelApplication: TravelAgencyApplication
        fun getTravelApplication() = travelApplication
    }
}