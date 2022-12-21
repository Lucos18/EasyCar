package com.example.myapplication.ui.addCar

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.CarColors
import com.example.myapplication.enums.fuelType
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddNewCarViewModelTest {
    private lateinit var viewModel: AddNewCarViewModel
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
        viewModel = AddNewCarViewModel(dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkBrandInput_returnsTrue() {
        assert(viewModel.checkBrandInput("ALFA ROMEO"))
    }

    @Test
    fun checkBrandInput_returnsFalse() {
        assertFalse(viewModel.checkBrandInput(""))
    }

    @Test
    fun checkYearInput_returnsTrue() {
        assert(viewModel.checkYearInput((1800..2022).random()))
    }

    @Test
    fun checkYearInput_returnsFalse() {
        assertFalse(viewModel.checkYearInput(0))
        assertFalse(viewModel.checkYearInput(null))
    }

    @Test
    fun checkModelInput_returnsTrue() {
        assert(viewModel.checkModelInput("Giulia"))
    }

    @Test
    fun checkModelInput_returnsFalse() {
        assertFalse(viewModel.checkModelInput(""))
    }

    @Test
    fun checkFuelInput_returnsTrue() {
        val randomNumber = (0 until fuelType.values().size).random()
        assert(viewModel.checkFuelInput(fuelType.values()[randomNumber].toString()))
    }

    @Test
    fun checkFuelInput_returnsFalse() {
        assertFalse(viewModel.checkBrandInput(""))
    }

    @Test
    fun checkPowerInput_returnsTrue() {
        val randomPower = (0..9999).random()
        assert(viewModel.checkPowerInput(randomPower))
    }

    @Test
    fun checkPowerInput_returnsFalse() {
        val randomPower = (10000..99999).random()
        assertFalse(viewModel.checkPowerInput(randomPower))
        assertFalse(viewModel.checkPowerInput(0))
        assertFalse(viewModel.checkPowerInput(null))
    }

    @Test
    fun checkSeatsInput_returnsTrue() {
        val randomSeats = (1..20).random()
        assert(viewModel.checkSeatsInput(randomSeats))
    }

    @Test
    fun checkSeatsInput_returnsFalse() {
        val randomSeats = (100..120).random()
        assertFalse(viewModel.checkSeatsInput(randomSeats))
        assertFalse(viewModel.checkSeatsInput(null))
        assertFalse(viewModel.checkSeatsInput(0))
    }

    @Test
    fun checkPriceInput_returnsTrue() {
        val randomPrice = (10000..120000).random()
        assert(viewModel.checkPriceInput(randomPrice.toDouble()))
    }

    @Test
    fun checkPriceInput_returnsFalse() {
        assertFalse(viewModel.checkPriceInput(0.0))
    }

    @Test
    fun checkMileageInput_returnsTrue() {
        val randomMileage = (10000..100000).random()
        assert(viewModel.checkMileageInput(randomMileage.toDouble()))
    }

    @Test
    fun checkMileageInput_returnsFalse() {
        assertFalse(viewModel.checkMileageInput(-1.0))
        assertFalse(viewModel.checkMileageInput(null))
    }

    @Test
    fun checkColorInput_returnsTrue() {
        val randomColor = CarColors.values().random()
        assert(viewModel.checkColorInput(randomColor.toString()))
    }

    @Test
    fun checkColorInput_returnsFalse() {
        assertFalse(viewModel.checkColorInput(""))
    }


    @Test
    fun convertKwToCv_returnsRightValue() {
        val input = (10..1000).random()
        assertEquals(viewModel.convertKwToCv(input), (input * 1.36).toInt())
    }

    @Test
    fun checkCarListValue() {
        viewModel.refreshDataFromNetwork()
        Thread.sleep(4000)
        assert(viewModel.checkCarListValue())
    }
}