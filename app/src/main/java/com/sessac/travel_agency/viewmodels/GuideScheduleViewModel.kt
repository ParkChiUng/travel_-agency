package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.repository.GuideScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class GuideScheduleViewModel : ViewModel() {

    private val guideScheduleRepository: GuideScheduleRepository by lazy {
        GuideScheduleRepository()
    }

    private var _guideLists = MutableLiveData<List<GuideItem>>()
    val guideLists get() = _guideLists

    private val ioDispatchers = CoroutineScope(Dispatchers.IO)

    fun insertGuideSchedule(guideScheduleItem: GuideScheduleItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                guideScheduleRepository.insertGuideSchedule(guideScheduleItem)
//                findPackageList(packageItem.status)
            }
        }
    }

    fun updateGuideSchedule(guideScheduleItem: GuideScheduleItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                guideScheduleRepository.updateGuideSchedule(guideScheduleItem)
//                findPackageList(packageItem.status)
            }
        }
    }

    fun deleteGuideSchedule(guideScheduleId: Int) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                guideScheduleRepository.deleteGuideSchedule(guideScheduleId)
            }.await()
        }
    }

    fun findGuideSchedule(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _guideLists.postValue(guideScheduleRepository.findGuideSchedule(startDate, endDate))
            }.await()
        }
    }
}