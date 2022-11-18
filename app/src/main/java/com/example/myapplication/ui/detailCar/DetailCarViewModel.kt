package com.example.myapplication.ui.detailCar

import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.ui.home.HomeViewModel

class DetailCarViewModel(private val carDao: CarDao) : ViewModel() {

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }
}
class DetailViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailCarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailCarViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}