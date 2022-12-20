package com.example.myapplication.ui.search

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.ui.addCar.AddNewCarViewModel
import com.example.myapplication.ui.detailCar.DetailCarViewModel
import com.example.myapplication.ui.favorites.FavoritesViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
        viewModel = SearchViewModel(dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getCurrentNumberOfResults() {
    }

    @Test
    fun onStateCarCheckChange_checkMapFiltersValue_newBeTrue() = runBlocking {
        viewModel.onStateCarCheckChange("New", true)
        assert(viewModel.mapFilters[CarFiltersSearch.NEW] == true)
    }

    @Test
    fun onStateCarCheckChange_checkMapFiltersValue_newBeFalse() = runBlocking {
        viewModel.onStateCarCheckChange("New", false)
        assert(viewModel.mapFilters[CarFiltersSearch.NEW] == false)
    }

    @Test
    fun onStateCarCheckChange_checkMapFiltersValue_UsedBeTrue() = runBlocking {
        viewModel.onStateCarCheckChange("Used", true)
        assert(viewModel.mapFilters[CarFiltersSearch.USED] == true)
    }
    @Test
    fun onStateCarCheckChange_checkMapFiltersValue_UsedBeFalse() = runBlocking {
        viewModel.onStateCarCheckChange("Used", false)
        assert(viewModel.mapFilters[CarFiltersSearch.USED] == false)
    }

    @Test
    fun onBrandChange_checkMapFiltersValue_brandBeTrue() {
        val chosenBrand = "ALFA ROMEO"
        viewModel.onBrandChange(chosenBrand)
        assert(viewModel.mapFilters[CarFiltersSearch.BRAND] == true)
        assert(viewModel.brandSelected == chosenBrand)
    }

    @Test
    fun onModelChange_checkMapFiltersValue_modelBeTrue() {
        val chosenModel = "Giulia"
        viewModel.onModelChange(chosenModel)
        assert(viewModel.mapFilters[CarFiltersSearch.MODEL] == true)
        assert(viewModel.modelSelected == chosenModel)
    }

    @Test
    fun onStartingPriceChange() {
        val chosenPrice = 100.0
        viewModel.onStartingPriceChange(chosenPrice)
        assert(viewModel.mapFilters[CarFiltersSearch.MINPRICE] == true)
        assert(viewModel.minPriceSelected == chosenPrice)
    }

    @Test
    fun onStartingPowerChange() {
        val chosenPower = 100.0
        viewModel.onStartingPowerChange(chosenPower)
        assert(viewModel.mapFilters[CarFiltersSearch.MIN_POWER] == true)
        assert(viewModel.minPowerSelected == chosenPower)
    }

    @Test
    fun onEndingPriceChange() {
        val chosenPrice = 100.0
        viewModel.onEndingPriceChange(chosenPrice)
        assert(viewModel.mapFilters[CarFiltersSearch.MAXPRICE] == true)
        assert(viewModel.maxPriceSelected == chosenPrice)
    }

    @Test
    fun onEndingPowerChange() {
        val chosenPower = 100.0
        viewModel.onEndingPowerChange(chosenPower)
        assert(viewModel.mapFilters[CarFiltersSearch.MAX_POWER] == true)
        assert(viewModel.maxPowerSelected == chosenPower)
    }

    @Test
    fun getDistinctBrandNames_checkValueFromInternet_returnMapOfMakers() = runBlocking {
        refreshDataFromNetwork()
        Thread.sleep(2000)
        assert(viewModel.getDistinctBrandNames().isNotEmpty())
    }
    @Test
    fun getDistinctBrandNames_checkValueFromInternet_returnEmptyMap() = runBlocking {
        refreshDataFromNetwork()
        Log.d("ciaoLista", viewModel.getDistinctBrandNames().toString())
        assert(viewModel.getDistinctBrandNames() == listOf(""))
    }

    @Test
    fun checkCarListValue_checkValueFromInternet_returnTrue() {
        refreshDataFromNetwork()
        Thread.sleep(2000)

        assert(viewModel.checkCarListValue())
    }

    @Test
    fun checkCarListValue_checkValueFromInternet_returnFalse() {
        refreshDataFromNetwork()
        assertFalse(viewModel.checkCarListValue())
    }

    @Test
    fun getDistinctModelByBrand() {
    }

    @Test
    fun onCheckDieselFilter() {
    }

    @Test
    fun onCheckElectricFilter() {
    }

    @Test
    fun onCheckPetrolFilter() {
    }

    @Test
    fun onCheckGasFilter() {
    }

    @Test
    fun onCheckBoxChange() {
    }

    @Test
    fun checkMaxIsNotMinorOfMin() {
    }

    @Test
    fun refreshDataFromNetwork() {
    }

    @Test
    fun filterListOfCars() {
    }

    @Test
    fun carCheck() {
    }
}