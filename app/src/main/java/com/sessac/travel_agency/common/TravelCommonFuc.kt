package com.sessac.travel_agency.common

import android.widget.Toast

fun commonToast(message: String){
    Toast.makeText(TravelAgencyApplication.getTravelApplication(), message, Toast.LENGTH_SHORT).show()
}