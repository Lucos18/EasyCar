package com.example.myapplication.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarInfo
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(val CarDao: CarDao) : ViewModel(){
    private val _carList = MutableLiveData<List<CarInfo>>()
    //TODO Create a new type of filter that works (maybe one that gets all the filters in a list if selected)
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    lateinit var filteredList: List<Car>
    fun onStateCarCheckChange(string: String): Int{
        when(string){
            "New" -> filteredList = allCars.value?.filter { car -> car.mileage == 0.0  }!!
            "Used" -> filteredList = allCars.value?.filter { car -> car.mileage > 0.0  }!!
        }
        return filteredList.size
    }
    fun onBrandChange(brand: String): Int{
        filteredList = allCars.value?.filter { car -> car.brand == brand }!!
        return filteredList.size
    }
    fun onModelChange(model: String): Int{
        filteredList = allCars.value?.filter { car -> car.model == model }!!
        return filteredList.size
    }
    fun getDistinctBrandNames(): List<String> {
        return _carList.value!!.map { e -> e.maker }.distinct().sorted()
    }
    fun getDistinctModelByBrand(maker: String): List<String> {
        val makerList = _carList.value!!.filter { e -> e.maker == maker }
        return makerList.map { e -> e.model }.distinct().sorted()
    }
    fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            _carList.value = VehicleApi.retrofitService.getCarInfo()

        } catch (networkError: IOException) {

        }
    }
}

class SearchViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}