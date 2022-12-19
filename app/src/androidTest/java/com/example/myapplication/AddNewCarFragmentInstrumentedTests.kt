package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.ui.addCar.AddNewCarFragment
import com.example.myapplication.ui.addCar.AddNewCarFragmentDirections
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class AddNewCarFragmentInstrumentedTest {
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val mockNavController = Mockito.mock(NavController::class.java)
    val randomCar = CarList.random()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun go_to_add_new_car_fragment_and_add_new_car() = runBlocking {
        val mockNavController = startAddNewCarFragment()
        //go_to_sell_fragment()
        //click_add_new_car_fab()
        Thread.sleep(60000)
        click_text_input_list_view_and_choose_by_text(R.id.car_brand_add_text, randomCar.brand)
        click_text_input_year_production_and_choose_year(R.id.car_year_add_text, randomCar.yearStartProduction)
        click_text_input_list_view_and_choose_by_text(R.id.car_model_add_text, randomCar.model)
        click_text_input_fuel_and_choose_by_text(R.id.car_fuel_type_add_text, randomCar.fuelType)
        click_text_input_and_write_string(R.id.car_power_add_text, randomCar.carPower.toString())
        click_text_input_and_write_string(R.id.car_seats_add_text, randomCar.seats.toString())
        click_text_input_and_write_string(R.id.car_price_add_text, randomCar.price.toString())
        click_text_input_and_write_string(R.id.car_mileage_add_text, randomCar.mileage.toString())
        click_text_input_color_and_choose_by_text(R.id.car_color_add_text, randomCar.color)
        Thread.sleep(2000)
        //TODO add image somewhat
        simple_click(R.id.button_add_new_car)
        Mockito.verify(mockNavController).navigate(
            AddNewCarFragmentDirections.actionAddNewCarFragmentToNavigationSell())
        dao.insert(randomCar)
        dao.getCars().test {
            val list = awaitItem()
            assert(list.contains(randomCar))
            cancel()
        }
    }

    @Test
    fun go_to_add_new_car_fragment_without_connection_check_error_constraint() {
        enableWifi(false)
        enableCellularData(false)
        Thread.sleep(3000)
        go_to_sell_fragment()
        click_add_new_car_fab()
        check_if_visible(R.id.image_error_connection)
        enableWifi(true)
        enableCellularData(true)
        Thread.sleep(3000)
        simple_click(R.id.retry_again_error_connection)
        check_if_visible(R.id.button_add_new_car)
    }
}
