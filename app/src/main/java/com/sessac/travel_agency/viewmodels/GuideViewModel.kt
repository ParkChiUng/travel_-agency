package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.repository.GuideRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuideViewModel : ViewModel(){

    private val repository: GuideRepository by lazy {
        GuideRepository()
    }

    private var _guideLists = MutableLiveData<List<GuideItem>>()
    val guideLists get() = _guideLists

//    private var _guide = MutableLiveData<GuideItem>()
//    val guide get() = _guide

    private val ioDispatchers = CoroutineScope(Dispatchers.IO)

    fun insertGuide(guide: GuideItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                repository.insertGuide(guide)
                findAllGuideList()
            }
        }
    }

    fun updateGuide(guide: GuideItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                repository.updateGuide(guide)
                findAllGuideList()
            }
        }
    }

    fun deleteGuide(guideId: Int) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                repository.deleteGuide(guideId)
                findAllGuideList()
            }.await()
        }
    }

    fun findAllGuideList() {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _guideLists.postValue(repository.findAllGuides())
            }.await()
        }
    }
}