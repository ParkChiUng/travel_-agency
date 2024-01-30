package com.sessac.travel_agency.common

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.sessac.travel_agency.database.AppDatabase
import com.sessac.travel_agency.helper.TravelAgencyOpenHelper

class TravelAgencyApplication : Application() {
//    lateinit var dbHelper: TravelAgencyOpenHelper
//    lateinit var db: SQLiteDatabase
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
//        dbHelper = TravelAgencyOpenHelper(applicationContext)
//        db = dbHelper.writableDatabase
        database = AppDatabase.getDatabase(this)
        travelApplication = this
    }

    companion object {
        private lateinit var travelApplication: TravelAgencyApplication
        fun getTravelApplication() = travelApplication
    }
}