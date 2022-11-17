package com.example.myapplication.ui.home

import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
//TODO CARDAO CAUSES CRASHES
class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
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