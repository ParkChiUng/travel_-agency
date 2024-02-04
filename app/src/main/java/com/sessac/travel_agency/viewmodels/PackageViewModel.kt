package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.repository.GuideScheduleRepository
import com.sessac.travel_agency.repository.LodgingRepository
import com.sessac.travel_agency.repository.PackageRepository
import com.sessac.travel_agency.repository.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class PackageViewModel : ViewModel(){

    private val packageRepository: PackageRepository by lazy {
        PackageRepository()
    }

    private val guideScheduleRepository: GuideScheduleRepository by lazy {
        GuideScheduleRepository()
    }

    private val lodgingRepository: LodgingRepository by lazy {
        LodgingRepository()
    }

    private val scheduleRepository: ScheduleRepository by lazy {
        ScheduleRepository()
    }


    /**
     * 패키지 리스트
     */
    private var _packageLists = MutableLiveData<List<PackageItem>>()
    val packageLists get() = _packageLists

    /**
     * insert한 패키지 Id
     */
    private var _package = MutableLiveData<Long>()
    val packageId get() = _package

    /**
     * 날짜에 따른 가이드 리스트
     */
    private var _guideLists = MutableLiveData<List<GuideItem>>()
    val guideLists get() = _guideLists

    /**
     * 숙소 리스트
     */
    private var _lodgingList = MutableLiveData<List<LodgingItem>>()
    val lodgingList get() = _lodgingList

    private var _lodgingItem = MutableLiveData<LodgingItem>()
    val lodgingItem get() = _lodgingItem

    /**
     * 스케줄 리스트
     */
    private var _schedule = MutableLiveData<List<ScheduleItem>>()
    val scheduleList get() = _schedule

    private val ioDispatchers = CoroutineScope(Dispatchers.IO)

    /**
     * 패키지
     */
    fun insertPackage(packageItem: PackageItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                _package.postValue(packageRepository.insertPackage(packageItem))
            }
        }
    }

    fun updatePackage(packageItem: PackageItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                packageRepository.updatePackage(packageItem)
            }
        }
    }

    fun deletePackage(packageId: Int) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                packageRepository.deletePackage(packageId)
            }.await()
        }
    }

    fun getPastPackages(currentDate: Date){
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _packageLists.postValue(packageRepository.getPastPackages(currentDate))
            }.await()
        }
    }

    fun getCurrentPackages(currentDate: Date){
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _packageLists.postValue(packageRepository.getCurrentPackages(currentDate))
            }.await()
        }
    }

    fun getFuturePackages(currentDate: Date){
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _packageLists.postValue(packageRepository.getFuturePackages(currentDate))
            }.await()
        }
    }


    /**
     * 가이드 스케줄
     */
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

    /**
     * 지역 명으로 전체 숙소 리스트 가져오기
     */
    fun findLodgingList(area: String) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _lodgingList.postValue(lodgingRepository.findLodgingsByArea(area))
            }.await()
        }
    }
    fun findLodging(id: Int) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _lodgingItem.postValue(lodgingRepository.findLodgingById(id))
            }.await()
        }
    }

    suspend fun findLodgingReturn(id: Int) : LodgingItem{
        return lodgingRepository.findLodgingByIdReturn(id)
    }

    /**
     * 스케줄
     */
    fun findSchedule(packageId: Int){
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                _schedule.postValue(scheduleRepository.findSchedules(packageId))
            }.await()
        }
    }

    fun insertSchedule(scheduleItem: ScheduleItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                scheduleRepository.insertSchedule(scheduleItem)
            }
        }
    }
}