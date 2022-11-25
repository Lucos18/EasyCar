package com.example.myapplication.ui.favorites

import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(private val CarDao: CarDao) : ViewModel() {
    //TODO Change it to favorites list item of a new database with a new dao
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    val allFavoritesCar: LiveData<List<Car>> = CarDao.getAllFavoritesCar().asLiveData()
    val favoritesCarNumber: LiveData<Int> = CarDao.getFavoritesCarNumber().asLiveData()
}
class FavoritesViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}