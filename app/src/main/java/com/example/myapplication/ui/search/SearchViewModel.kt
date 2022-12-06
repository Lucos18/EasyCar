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

    var modelSelected: String = ""
    var brandSelected: String = ""
    var maxPriceSelected: Double = 0.0
    var minPriceSelected: Double = 0.0
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    var filteredList: List<Car>? = allCars.value?.toList()
    fun onStateCarCheckChange(string: String, valueToSet: Boolean): Int? {
        if(string == "New"){
            mapFilters[CarFiltersSearch.NEW] = valueToSet
            mapFilters[CarFiltersSearch.USED] = false
            Log.d("mapfilters", mapFilters[CarFiltersSearch.NEW].toString())
            Log.d("mapfilters", mapFilters[CarFiltersSearch.USED].toString())

        } else if (string == "Used") {
            mapFilters[CarFiltersSearch.USED] = valueToSet
            Log.d("mapfilters", mapFilters[CarFiltersSearch.NEW].toString())
            Log.d("mapfilters", mapFilters[CarFiltersSearch.USED].toString())
        }
        return filterListOfCars()
    }

    fun onBrandChange(brand: String): Int? {
        mapFilters[CarFiltersSearch.BRAND] = true
        brandSelected = brand
        return filterListOfCars()
    }

    fun onModelChange(model: String): Int? {
        mapFilters[CarFiltersSearch.MODEL] = true
        modelSelected = model
        return filterListOfCars()
    }

    fun onStartingPriceChange(minPrice: Double): Int?{
        mapFilters[CarFiltersSearch.MINPRICE] = true
        minPriceSelected = minPrice
        return filterListOfCars()
    }

    fun onEndingPriceChange(maxPrice: Double): Int?{
        mapFilters[CarFiltersSearch.MAXPRICE] = true
        maxPriceSelected = maxPrice
        return filterListOfCars()
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

    fun filterListOfCars(): Int? {
        //TODO Filter by true or false
        //TODO Return number of cars found with filter
        //TODO Do something about the next fragment where you show the cars
        filteredList = allCars.value?.toList()
        mapFilters.forEach { filter ->

            when (filter.key){
                CarFiltersSearch.NEW -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage == 0.0 }
                CarFiltersSearch.USED -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage > 0.0 }
                CarFiltersSearch.MODEL -> if (filter.value) filteredList = filteredList?.filter { car -> car.model == modelSelected }
                CarFiltersSearch.BRAND -> if (filter.value) filteredList = filteredList?.filter { car -> car.brand == brandSelected }
                CarFiltersSearch.MINPRICE -> if (filter.value) filteredList = filteredList?.filter { car -> car.price >= minPriceSelected }
                CarFiltersSearch.MAXPRICE -> if (filter.value) filteredList = filteredList?.filter { car -> car.price <= maxPriceSelected }
            }
        }
        Log.d("filter", brandSelected)
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