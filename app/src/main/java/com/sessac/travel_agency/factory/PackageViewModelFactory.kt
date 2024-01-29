package com.sessac.travel_agency.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sessac.travel_agency.repository.PackageRepository
import com.sessac.travel_agency.viewmodels.PackageViewModel

class PackageViewModelFactory(private val repository: PackageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PackageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PackageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}