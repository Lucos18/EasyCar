package com.example.myapplication.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarInfo
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(val CarDao: CarDao) : ViewModel(){
    private val _carList = MutableLiveData<List<CarInfo>>()
    //TODO Create a new type of filter that works (maybe one that gets all the filters in a list if selected)
    val mapFilters = mutableMapOf<CarFiltersSearch, Boolean>()

    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    fun onStateCarCheckChange(string: String): Int? {
        if(string == "New"){
            mapFilters[CarFiltersSearch.NEW] = true
            mapFilters[CarFiltersSearch.USED] = false
        } else if (string == "Used") {
            mapFilters[CarFiltersSearch.NEW] = false
            mapFilters[CarFiltersSearch.USED] = true
        }
        return filterListOfCars(null,null)
    }
    fun onBrandChange(brand: String): Int? {
        mapFilters[CarFiltersSearch.BRAND] = true
        return filterListOfCars(brand = brand, null)
    }
    fun onModelChange(model: String): Int? {
        mapFilters[CarFiltersSearch.MODEL] = true
        return filterListOfCars(null, model = model)
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
    fun filterListOfCars(brand:String?, model: String?): Int? {
        //TODO Filter by true or false
        //TODO Return number of cars found with filter
        //TODO Do something about the next fragment where you show the cars
        var filteredList: List<Car>? = allCars.value?.toList()
        mapFilters.forEach { filter ->

            when (filter.key){
                CarFiltersSearch.NEW -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage == 0.0 }
                CarFiltersSearch.USED -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage > 0.0 }
                CarFiltersSearch.MODEL -> if (filter.value) filteredList = filteredList?.filter { car -> car.model == model }
                CarFiltersSearch.BRAND -> if (filter.value) filteredList = filteredList?.filter { car -> car.brand == brand }
                CarFiltersSearch.MINPRICE -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage > 0.0 }
                else -> {}
            }
        }
        Log.d("filter", brand.toString())
        Log.d("filter", filteredList.toString())
        return filteredList?.size
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