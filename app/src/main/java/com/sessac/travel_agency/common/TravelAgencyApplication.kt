package com.sessac.travel_agency.common

import android.app.Application
import com.sessac.travel_agency.database.AppDatabase

class TravelAgencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //tutor pyo coroutine
        database = AppDatabase.getDatabase(this)
        travelApplication = this
    }

    companion object {
        private lateinit var travelApplication: TravelAgencyApplication
        private lateinit var database: AppDatabase
        fun getTravelApplication() = travelApplication
        fun  getRoomDatabase() = database
    }
}