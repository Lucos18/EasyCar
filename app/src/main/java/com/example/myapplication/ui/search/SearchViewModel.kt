package com.example.myapplication.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.CarDao

class SearchViewModel(private val CarDao: CarDao) : ViewModel()

class SearchViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}