package com.sessac.travel_agency.common

import android.app.Application
import com.sessac.travel_agency.database.AppDatabase

class TravelAgencyApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        travelApplication = this
    }

    companion object {
        private lateinit var travelApplication: TravelAgencyApplication
        fun getTravelApplication() = travelApplication
    }
}