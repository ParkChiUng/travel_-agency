package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.repository.LodgingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 비동기적으로 데이터를 처리하고 Lodging UI에 업데이트를 알리는 데 사용.
 * Lodging Repository와 상호 작용하여 DB작업을 수행하고 결과를 LiveData로 제공함.
 * */
class LodgingViewModel : ViewModel() {

    private val repository: LodgingRepository by lazy {
        LodgingRepository()
    }

    private var _lodgingList = MutableLiveData<List<LodgingItem>>()
    val lodgingList get() = _lodgingList

    private var _lodging = MutableLiveData<LodgingItem>()
    val lodging get() = _lodging

    /**
     * 별도의 CoroutineScope를 사용하지 않고
     * viewModelScope 내에서 바로 IO 디스패처를 지정하여 작업을 수행
     * */
    fun insertLodging(lodging: LodgingItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertLodging(lodging)
                findAllLodgingList()
            }
        }
    }

    fun updateLodging(lodging: LodgingItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateLodging(lodging)
                findAllLodgingList()
            }
        }
    }

    fun deleteLodging(lodging: LodgingItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteLodging(lodging)
                findAllLodgingList()
            }
        }
    }

    fun findAllLodgingList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _lodgingList.postValue(repository.findAllLodgings())
            }
        }
    }

    fun findLodgingsByArea(area: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _lodgingList.postValue(repository.findLodgingsByArea(area))
            }
        }
    }
}