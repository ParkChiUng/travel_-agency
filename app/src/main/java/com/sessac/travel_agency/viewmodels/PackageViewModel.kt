package com.sessac.travel_agency.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.repository.PackageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PackageViewModel(private val repository: PackageRepository) : ViewModel(){

//    val allPackageList: List<PackageItem> get() = repository.allPackageList

    val allPackageList = MutableLiveData<List<PackageItem>>()

    fun getAllPackage() {
        viewModelScope.launch(Dispatchers.IO) {
            allPackageList.postValue(repository.getPackageList())
        }
    }
}