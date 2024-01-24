package com.sessac.travel_agency.common

import android.app.Application

class TravelAgencyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        travelApplication = this
    }
    companion object{
        private lateinit var travelApplication: TravelAgencyApplication
        fun getTravelApplication() = travelApplication
    }
}