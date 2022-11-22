package com.example.myapplication.ui.sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car

class SellViewModel(CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
}

class SellViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SellViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}