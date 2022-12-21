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
import kotlin.math.max

class SearchViewModel(val CarDao: CarDao) : ViewModel(){
    private val _carList = MutableLiveData<List<CarInfo>>()
    val mapFilters = mutableMapOf<CarFiltersSearch, Boolean>()
    val mapFiltersFuelType = mutableMapOf<fuelType, Boolean>()
    var modelSelected: String = ""
    var brandSelected: String = ""
    var maxPriceSelected: Double = 0.0
    var minPriceSelected: Double = 0.0
    var maxPowerSelected: Double = 0.0
    var minPowerSelected: Double = 0.0
    var multiplierPower: Double = 1.00
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    var filteredList: List<Car>? = allCars.value?.toList()
    val currentNumberOfResults = MutableLiveData(filteredList?.size ?: 0)
    fun onStateCarCheckChange(string: String, valueToSet: Boolean){
        if(string == "New"){
            mapFilters[CarFiltersSearch.NEW] = valueToSet
            mapFilters[CarFiltersSearch.USED] = false
        } else if (string == "Used") {
            mapFilters[CarFiltersSearch.USED] = valueToSet
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

    fun onStartingPowerChange(minPower: Double){
        mapFilters[CarFiltersSearch.MIN_POWER] = true
        minPowerSelected = minPower
        filterListOfCars()
    }

    fun onEndingPriceChange(maxPrice: Double){
        if(maxPrice == 0.0){
            mapFilters[CarFiltersSearch.MAXPRICE] = false
        } else {
            mapFilters[CarFiltersSearch.MAXPRICE] = true
            maxPriceSelected = maxPrice
        }
        filterListOfCars()
    }

    fun onEndingPowerChange(maxPower: Double){
        if(maxPower == 0.0)
        {
            mapFilters[CarFiltersSearch.MAX_POWER] = false
        } else {
            mapFilters[CarFiltersSearch.MAX_POWER] = true
            maxPowerSelected = maxPower
        }
        filterListOfCars()
    }

    fun getDistinctBrandNames(): List<String> {
        if (checkCarListValue())
        {
            return _carList.value!!.map { e -> e.maker }.distinct().sorted()
        }
        return listOf("")
    }
    fun checkCarListValue(): Boolean {
        return _carList.value != null
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

    fun checkMaxIsNotMinorOfMin(Min: Double?, Max: Double?):Boolean{
        if (Min == null || Max == null) return true
        return Max >= Min
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
                CarFiltersSearch.MIN_POWER -> if (filter.value) filteredList = filteredList?.filter { car -> (car.carPower * multiplierPower) >= minPowerSelected.toInt()}
                CarFiltersSearch.MAX_POWER -> if (filter.value) filteredList = filteredList?.filter { car -> (car.carPower * multiplierPower) <= maxPowerSelected.toInt()}
                CarFiltersSearch.AT_LEAST_ONE_PHOTO -> if (filter.value) filteredList = filteredList?.filter { car -> car.image != null }
                else -> 0
            }
        }
        filteredList = carCheck(filteredList)
        currentNumberOfResults.postValue(filteredList?.size)
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