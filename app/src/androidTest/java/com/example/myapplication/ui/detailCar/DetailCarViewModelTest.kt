package com.example.myapplication.ui.detailCar

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.myapplication.CarListDao
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.fuelType
import com.example.myapplication.ui.addCar.AddNewCarViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class DetailCarViewModelTest {
    private lateinit var viewModel: DetailCarViewModel
    private lateinit var viewModelAdd: AddNewCarViewModel
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
        viewModel = DetailCarViewModel(dao)
        viewModelAdd = AddNewCarViewModel(dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getCarLogos() {
    }

    @Test
    fun getCarById() {


    }

    @Test
    fun deleteCarById() = runBlocking {
        viewModelAdd.addCarComplete(CarListDao[0])
        dao.getCars().test {
            val list = awaitItem()
            Log.d("ciaoLista", list.toString())
            assert(list.isEmpty())
            cancel()
        }
        viewModel.deleteCarById(CarListDao[0].id)
        dao.getCars().test {
            val list = awaitItem()
            Log.d("ciaoLista", list.toString())
            assert(list.isEmpty())
            cancel()
        }
    }

    @Test
    fun refreshDataFromNetwork() {
    }

    @Test
    fun updateCar() {
    }

    @Test
    fun updateCarDatabase() {
        viewModel.updateCarDatabase(CarListDao[0])
    }

    @Test
    fun checkInputEditTextNewCar_returnsTrue() {
        val year = (1800..2022).random()
        val fuelType = fuelType.values().random()
        val power = (1..1000).random()
        val seats = (1..20).random()
        val price = (1000..100000).random()
        val mileage = (0..100000).random()
        assert(
            viewModel.checkInputEditTextNewCar(
                year,
                fuelType.toString(),
                power,
                seats,
                price.toDouble(),
                mileage.toDouble()
            )
        )
    }
    @Test
    fun checkInputEditTextNewCar_returnsFalse_allWrongInputs() {
        val year = 0
        val fuelType = ""
        val power = 0
        val seats = 120
        val price = 0
        val mileage = -1
        assertFalse(
            viewModel.checkInputEditTextNewCar(
                year,
                fuelType,
                power,
                seats,
                price.toDouble(),
                mileage.toDouble()
            )
        )
    }
    @Test
    fun checkInputEditTextNewCar_returnsFalse_singleWrongValue() {
        val year = (1800..2022).random()
        val fuelType = fuelType.values().random()
        val power = (1..1000).random()
        val seats = 120
        val price = (1000..100000).random()
        val mileage = (0..100000).random()
        assertFalse(
            viewModel.checkInputEditTextNewCar(
                year,
                fuelType.toString(),
                power,
                seats,
                price.toDouble(),
                mileage.toDouble()
            )
        )
    }
}