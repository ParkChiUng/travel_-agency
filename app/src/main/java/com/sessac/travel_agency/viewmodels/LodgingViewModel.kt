package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.repository.LodgingRepository

class LodgingViewModel {
    private val repository: LodgingRepository by lazy {
        LodgingRepository()
    }

    private var _lodgingLists = MutableLiveData<List<LodgingItem>>()

}