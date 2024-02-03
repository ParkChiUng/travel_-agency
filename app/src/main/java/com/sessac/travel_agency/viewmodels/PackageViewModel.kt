package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.repository.GuideScheduleRepository
import com.sessac.travel_agency.repository.PackageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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


    // 패키지
    private var _packageLists = MutableLiveData<List<PackageItem>>()
    val packageLists get() = _packageLists

//    private var _package = MutableLiveData<PackageItem>()
//    val packageItem get() = _package

    // 가이드
    private var _guideLists = MutableLiveData<List<GuideItem>>()
    val guideLists get() = _guideLists

    private val ioDispatchers = CoroutineScope(Dispatchers.IO)

    /**
     * 패키지
     */
    fun insertPackage(packageItem: PackageItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                packageRepository.insertPackage(packageItem)
//                findPackageList(packageItem.status)
            }
        }
    }

    fun updatePackage(packageItem: PackageItem) {
        viewModelScope.launch {
            withContext(ioDispatchers.coroutineContext) {
                packageRepository.updatePackage(packageItem)
//                findPackageList(packageItem.status)
            }
        }
    }

    fun deletePackage(packageId: Int) {
        viewModelScope.launch {
            async(ioDispatchers.coroutineContext) {
                packageRepository.deletePackage(packageId)
//                findPackageList(1)
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

//    fun findPackageList(status: Int) {
//        viewModelScope.launch {
//            async(ioDispatchers.coroutineContext) {
//                _packageLists.postValue(packageRepository.findPackage(status))
//            }.await()
//        }
//    }


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
}