package com.example.myapplication.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.enums.fuelType
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarInfo
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(val CarDao: CarDao) : ViewModel(){
    private val _carList = MutableLiveData<List<CarInfo>>()
    val mapFilters = mutableMapOf<CarFiltersSearch, Boolean>()
    private val mapFiltersFuelType = mutableMapOf<fuelType, Boolean>()
    var modelSelected: String = ""
    var brandSelected: String = ""
    var maxPriceSelected: Double = 0.0
    var minPriceSelected: Double = 0.0

    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    var filteredList: List<Car>? = allCars.value?.toList()
    val currentNumberOfResults = MutableLiveData(filteredList?.size ?: 0)
    fun onStateCarCheckChange(string: String, valueToSet: Boolean){
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
        filterListOfCars()
    }

    fun onBrandChange(brand: String){
        mapFilters[CarFiltersSearch.BRAND] = true
        brandSelected = brand
        filterListOfCars()
    }

    fun onModelChange(model: String){
        mapFilters[CarFiltersSearch.MODEL] = true
        modelSelected = model
        filterListOfCars()
    }

    fun onStartingPriceChange(minPrice: Double){
        mapFilters[CarFiltersSearch.MINPRICE] = true
        minPriceSelected = minPrice
        filterListOfCars()
    }

    fun onEndingPriceChange(maxPrice: Double){
        mapFilters[CarFiltersSearch.MAXPRICE] = true
        maxPriceSelected = maxPrice
        filterListOfCars()
    }

    fun getDistinctBrandNames(): List<String> {
        return _carList.value!!.map { e -> e.maker }.distinct().sorted()
    }

    fun getDistinctModelByBrand(maker: String): List<String> {
        val makerList = _carList.value!!.filter { e -> e.maker == maker }
        return makerList.map { e -> e.model }.distinct().sorted()
    }
    fun onCheckDieselFilter(isChecked: Boolean){
        mapFilters[CarFiltersSearch.DIESEL] = isChecked
        mapFiltersFuelType[fuelType.Diesel] = isChecked
        filterListOfCars()
    }
    fun onCheckElectricFilter(isChecked: Boolean){
        mapFilters[CarFiltersSearch.ELECTRIC] = isChecked
        mapFiltersFuelType[fuelType.Electric] = isChecked
        filterListOfCars()
    }
    fun onCheckPetrolFilter(isChecked: Boolean){
        mapFilters[CarFiltersSearch.PETROL] = isChecked
        mapFiltersFuelType[fuelType.Petrol] = isChecked
        filterListOfCars()
    }
    fun onCheckGasFilter(isChecked: Boolean){
        mapFilters[CarFiltersSearch.GAS] = isChecked
        mapFiltersFuelType[fuelType.Gas] = isChecked
        filterListOfCars()
    }
    fun onCheckBoxChange(isChecked: Boolean){
        mapFilters[CarFiltersSearch.AT_LEAST_ONE_PHOTO] = isChecked
        filterListOfCars()
    }

    fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            _carList.value = VehicleApi.retrofitService.getCarInfo()

        } catch (networkError: IOException) {

        }
    }

    fun filterListOfCars(): Int? {
        filteredList = allCars.value?.toList()
        mapFilters.forEach { filter ->

            when (filter.key){
                CarFiltersSearch.NEW -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage == 0.0 }
                CarFiltersSearch.USED -> if (filter.value) filteredList = filteredList?.filter { car -> car.mileage > 0.0 }
                CarFiltersSearch.MODEL -> if (filter.value) filteredList = filteredList?.filter { car -> car.model == modelSelected }
                CarFiltersSearch.BRAND -> if (filter.value) filteredList = filteredList?.filter { car -> car.brand == brandSelected }
                CarFiltersSearch.MINPRICE -> if (filter.value) filteredList = filteredList?.filter { car -> car.price >= minPriceSelected }
                CarFiltersSearch.MAXPRICE -> if (filter.value) filteredList = filteredList?.filter { car -> car.price <= maxPriceSelected }
                CarFiltersSearch.AT_LEAST_ONE_PHOTO -> if (filter.value) filteredList = filteredList?.filter { car -> car.image != null }
                else -> 0
            }
        }
        Log.d("ciao",filteredList.toString())
        filteredList = carCheck(filteredList)
        Log.d("filter", brandSelected)
        Log.d("filter", filteredList.toString())
        currentNumberOfResults.value = filteredList?.size
        return filteredList?.size
    }

    fun carCheck(filteredList: List<Car>?): List<Car>? {
        var mapFiltersFuelTypeOnlyTrue = mutableListOf<String>()
        var booleanFound = false
        mapFiltersFuelType.forEach { filter ->
            if(filter.value){
                booleanFound = true
                mapFiltersFuelTypeOnlyTrue.add(filter.key.toString())
            }
        }
        Log.d("ciao", mapFiltersFuelTypeOnlyTrue.toString())
        return if (booleanFound){
            mapFiltersFuelTypeOnlyTrue = mapFiltersFuelTypeOnlyTrue.toSet().toMutableList()
            filteredList?.filter{ it.fuelType in (mapFiltersFuelTypeOnlyTrue) }
        }else filteredList
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