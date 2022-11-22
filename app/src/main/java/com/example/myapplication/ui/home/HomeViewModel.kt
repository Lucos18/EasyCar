package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car

class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    fun getYearToString(year: Int): String = year.toString()
}

class HomeViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
