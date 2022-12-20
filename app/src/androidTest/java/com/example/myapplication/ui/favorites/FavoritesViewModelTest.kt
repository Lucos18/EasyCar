package com.example.myapplication.ui.favorites

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.CarListDao
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.ui.addCar.AddNewCarViewModel
import com.example.myapplication.ui.detailCar.DetailCarViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class FavoritesViewModelTest {
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
        viewModel = FavoritesViewModel(dao)
    }

    @Test
    fun getCarLogos() {
        val initialCarLogo = viewModel.carLogos.value
        refreshDataFromNetwork()
        Thread.sleep(2000)
        assert(viewModel.carLogos.value != initialCarLogo)
    }

    @Test
    fun refreshDataFromNetwork() {
    }

    @Test
    fun getAllFavoritesCar() {

    }

    @Test
    fun getFavoritesCarNumber() {
    }

    @Test
    fun updateFavorites() {
    }

    @Test
    fun updateCarDatabase() {
    }

    @Test
    fun restoreFavorite() = runBlocking {
        dao.insert(CarListDao[1])
        viewModel.restoreFavorite(CarListDao[1])
    }
}