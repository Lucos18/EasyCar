package com.example.myapplication.ui.addCar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.CarDao
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.ui.detailCar.DetailCarViewModel

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {
    fun checkInputEditTextNewCar(brand: String): Boolean{
        return brand.isNotBlank()
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