package com.example.myapplication.ui.addCar

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarInfo
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {
    private val _carList = MutableLiveData<List<CarInfo>>()

    val playlist: LiveData<List<CarInfo>>
        get() = _carList

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    fun checkInputEditTextNewCar(
        brand: String,
        year: Int,
        model: String,
        fuelType: String,
        power: Int,
        seats: Int,
        price: Double
    ): Boolean {
        return brand.isNotBlank() && year != 0 && model.isNotBlank() && fuelType.isNotBlank() && power != 0 && seats != 0 && price != 0.0
    }

    fun addCar(
        Brand: String,
        Model: String,
        YearStartProduction: Int,
        YearEndProduction: Int?,
        Seats: Int,
        CarPower: Int,
        FuelType: String,
        Price: Double,
        Image: Bitmap?
    ) {
        val car = Car(
            brand = Brand,
            model = Model,
            yearStartProduction = YearStartProduction,
            yearEndProduction = YearEndProduction,
            seats = Seats,
            carPower = CarPower,
            fuelType = FuelType,
            price = Price,
            image = Image?.toByteArray()
        )

        viewModelScope.launch {
            carDao.insert(car)
        }
    }

    fun refreshDataFromNetwork()
        = viewModelScope.launch {
        try {
            _carList.value = VehicleApi.retrofitService.getCarInfo()
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    fun getDistinctBrandNames(): List<String> {
        return _carList.value!!.map { e -> e.maker }.distinct().sorted()
    }

    fun getDistinctMaxYearCarByBrand(maker: String): String? {
        val makerList = _carList.value!!.filter { e -> e.maker == maker }
        return makerList.maxOfOrNull { e -> e.year }
    }

    fun getDistinctModelByBrandAndYear(maker: String, year: String): List<String> {
        val makerList = _carList.value!!.filter { e -> e.maker == maker }
        val yearList = makerList.filter { e -> e.year == year }
        return yearList.map { e -> e.fullModelName }.distinct().sorted()
    }

    fun convertKwToCv(kw: Int): Int {
        return (kw * 1.36).toInt()
    }

    private fun Bitmap.toByteArray(quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

}

class AddNewCarViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewCarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewCarViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}