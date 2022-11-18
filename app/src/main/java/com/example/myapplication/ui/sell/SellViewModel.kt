package com.example.myapplication.ui.sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.CarDao
import com.example.myapplication.ui.search.SearchViewModel

class SellViewModel(private val CarDao: CarDao) : ViewModel() {

}
class SellViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SellViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}