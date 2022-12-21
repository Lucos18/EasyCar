package com.example.myapplication.ui.search

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.myapplication.CarListDao
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.enums.fuelType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
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
        viewModel.refreshDataFromNetwork()
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
        viewModel.refreshDataFromNetwork()
        Thread.sleep(2000)
        assert(viewModel.getDistinctBrandNames().isNotEmpty())
    }

    @Test
    fun getDistinctBrandNames_checkValueFromInternet_returnEmptyMap() = runBlocking {
        viewModel.refreshDataFromNetwork()
        Log.d("ciaoLista", viewModel.getDistinctBrandNames().toString())
        assert(viewModel.getDistinctBrandNames() == listOf(""))
    }

    @Test
    fun checkCarListValue_checkValueFromInternet_returnTrue() {
        viewModel.refreshDataFromNetwork()
        Thread.sleep(4000)
        assert(viewModel.checkCarListValue())
    }

    @Test
    fun checkCarListValue_checkValueFromInternet_returnFalse() {
        viewModel.refreshDataFromNetwork()
        assertFalse(viewModel.checkCarListValue())
    }

    //TODO FIX LATER
    @Test
    fun getDistinctModelByBrand() {
        val maker = "ALFA ROMEO"
        val modelToSearch = "Giulia"
        viewModel.refreshDataFromNetwork()
        Thread.sleep(3000)
        Log.d("ciaoLista", viewModel.getDistinctModelByBrand("ALFA ROMEO").toString())
        assert(viewModel.getDistinctModelByBrand("ALFA ROMEO").isNotEmpty())
    }

    @Test
    fun onCheckDieselFilter_checkMapFiltersValue_dieselBeTrue() {
        viewModel.onCheckDieselFilter(true)
        assert(viewModel.mapFilters[CarFiltersSearch.DIESEL] == true)
        assert(viewModel.mapFiltersFuelType[fuelType.Diesel] == true)
    }

    @Test
    fun onCheckDieselFilter_checkMapFiltersValue_dieselBeFalse() {
        viewModel.onCheckDieselFilter(false)
        assert(viewModel.mapFilters[CarFiltersSearch.DIESEL] == false)
        assert(viewModel.mapFiltersFuelType[fuelType.Diesel] == false)
    }

    @Test
    fun onCheckElectricFilter_checkMapFiltersValue_electricBeTrue() {
        viewModel.onCheckElectricFilter(true)
        assert(viewModel.mapFilters[CarFiltersSearch.ELECTRIC] == true)
        assert(viewModel.mapFiltersFuelType[fuelType.Electric] == true)
    }

    @Test
    fun onCheckElectricFilter_checkMapFiltersValue_electricBeFalse() {
        viewModel.onCheckElectricFilter(false)
        assert(viewModel.mapFilters[CarFiltersSearch.ELECTRIC] == false)
        assert(viewModel.mapFiltersFuelType[fuelType.Electric] == false)
    }

    @Test
    fun onCheckPetrolFilter_checkMapFiltersValue_petrolBeTrue() {
        viewModel.onCheckPetrolFilter(true)
        assert(viewModel.mapFilters[CarFiltersSearch.PETROL] == true)
        assert(viewModel.mapFiltersFuelType[fuelType.Petrol] == true)
    }

    @Test
    fun onCheckPetrolFilter_checkMapFiltersValue_petrolBeFalse() {
        viewModel.onCheckPetrolFilter(false)
        assert(viewModel.mapFilters[CarFiltersSearch.PETROL] == false)
        assert(viewModel.mapFiltersFuelType[fuelType.Petrol] == false)
    }

    @Test
    fun onCheckGasFilter_checkMapFiltersValue_petrolBeTrue() {
        viewModel.onCheckPetrolFilter(true)
        assert(viewModel.mapFilters[CarFiltersSearch.PETROL] == true)
        assert(viewModel.mapFiltersFuelType[fuelType.Petrol] == true)
    }

    @Test
    fun onCheckGasFilter_checkMapFiltersValue_petrolBeFalse() {
        viewModel.onCheckPetrolFilter(false)
        assert(viewModel.mapFilters[CarFiltersSearch.PETROL] == false)
        assert(viewModel.mapFiltersFuelType[fuelType.Petrol] == false)
    }

    @Test
    fun onCheckBoxChange_checkMapFiltersValue_True() {
        viewModel.onCheckBoxChange(true)
        assert(viewModel.mapFilters[CarFiltersSearch.AT_LEAST_ONE_PHOTO] == true)
    }

    @Test
    fun checkMaxIsNotMinorOfMin_returnTrueValue() {
        val min = (1..100).random()
        val max = (100..200).random()
        assert(viewModel.checkMaxIsNotMinorOfMin(min.toDouble(), max.toDouble()))
    }

    @Test
    fun checkMaxIsNotMinorOfMin_returnTrueValueWithWrongValues() {
        val min = null
        val max = null
        assert(viewModel.checkMaxIsNotMinorOfMin(min, 10.0))
        assert(viewModel.checkMaxIsNotMinorOfMin(10.0, max))
        assert(viewModel.checkMaxIsNotMinorOfMin(min, max))
    }

    @Test
    fun filterListOfCars_returnAllCars_noFilters() {
        viewModel.refreshDataFromNetwork()
        Thread.sleep(5000)
        assert(viewModel.filterListOfCars()!! > 0)
    }

    @Test
    fun filterListOfCars_returnSomeCars_NewAndMinPriceFilters() = runBlocking {
        viewModel.mapFilters[CarFiltersSearch.MINPRICE] = true
        viewModel.mapFilters[CarFiltersSearch.NEW] = true
        //CarList0 = New - 40000
        dao.insert(CarListDao[0])
        //CarList1 = Used - 10000
        dao.insert(CarListDao[1])
        dao.getCars().test {
            val list = awaitItem()
            viewModel.filteredList = list
            val number = viewModel.filterListOfCars()
            Log.d("ciaoLista", number.toString())
            Log.d("ciaoLista", list.toString())
            cancel()
        }
    }

    @Test
    fun carCheck_returnSomeCars_DieselAndPetrolFilters() = runBlocking {
        viewModel.mapFiltersFuelType[fuelType.Diesel] = true
        viewModel.mapFiltersFuelType[fuelType.Petrol] = true
        viewModel.mapFilters[CarFiltersSearch.NEW] = true
        //CarList0 = New - 40000 - Diesel
        val car1 = CarListDao[0]
        dao.insert(car1)
        //CarList1 = Used - 10000 - Petrol
        val car2 = CarListDao[1]
        dao.insert(car2)
        //CarList3 = Used - 40000 - Electric
        val car3 = CarListDao[3]
        dao.insert(car3)
        dao.getCars().test {
            val list = awaitItem()
            assert(viewModel.carCheck(list)?.size != list.size)
            assert(viewModel.carCheck(list)!!.contains(car1))
            assert(viewModel.carCheck(list)!!.contains(car2))
            assertFalse(viewModel.carCheck(list)!!.contains(car3))
            cancel()
        }
    }
}