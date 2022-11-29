package com.example.myapplication.ui.detailCar

import android.graphics.ColorSpace
import android.media.Image
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarInfo
import com.example.myapplication.model.CarLogo
import com.example.myapplication.model.fuelType
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Year

class DetailCarViewModel(private val carDao: CarDao) : ViewModel() {
    private val _carLogo = MutableLiveData<List<CarLogo>>()

    val carLogos: LiveData<List<CarLogo>>
        get() = _carLogo

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }

    fun deleteCarById(id: Long) {
        viewModelScope.launch {
            carDao.deleteCarById(id)
        }
    }
    init {
        refreshDataFromNetwork()
    }
    fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            _carLogo.value = VehicleApi.retrofitServiceLogos.getCarLogos()
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    fun updateCar(
        car: Car,
        year: String,
        power: String,
        seats: String,
        fuelType: String,
        price: String,
        mileage: String
    ): Boolean {
        val updatedCar = Car(
            id = car.id,
            brand = car.brand,
            model = car.model,
            yearStartProduction = year.toInt(),
            yearEndProduction = car.yearEndProduction,
            seats = seats.toInt(),
            carPower = power.toInt(),
            fuelType = fuelType,
            price = price.toDouble(),
            mileage = mileage.toDouble(),
            image = car.image,
        )
        updateCarDatabase(updatedCar)
        return true
    }

    fun updateCarDatabase(car: Car) {
        CoroutineScope(Dispatchers.IO).launch {
            carDao.update(car)
        }
    }

    fun checkInputEditTextNewCar(
        year: Int,
        fuelType: String,
        power: Int,
        seats: Int,
        price: Double,
        mileage: Double
    ): Boolean {
        return year != 0 && fuelType.isNotBlank() && power != 0 && power.toString().length <= 4 && seats != 0 && price != 0.0 && mileage >= 0
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