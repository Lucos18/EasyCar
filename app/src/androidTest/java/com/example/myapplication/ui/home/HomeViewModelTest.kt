package com.example.myapplication.ui.home

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.ui.favorites.FavoritesViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
        viewModel = HomeViewModel(dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllCars() {
    }

    @Test
    fun getCarLogos() {
        val initialCarLogo = viewModel.carLogos.value
        viewModel.refreshDataFromNetwork()
        Thread.sleep(2000)
        assert(viewModel.carLogos.value != initialCarLogo)
    }

    @Test
    fun updateFavorites() {
    }

    @Test
    fun updateCarDatabase() {
    }
}