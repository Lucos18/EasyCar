package com.example.myapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.addCar.AddNewCarFragment
import com.example.myapplication.ui.addCar.AddNewCarFragmentDirections
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class AddNewCarFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val mockNavController = Mockito.mock(NavController::class.java)
    val randomCar = CarList.random()

    @Test
    fun go_to_add_new_car_fragment_and_add_new_car() {
        val mockNavController = startAddNewCarFragment()
        //go_to_sell_fragment()
        //click_add_new_car_fab()
        //TODO check current fragment
        click_text_input_list_view_and_choose_by_text(R.id.car_brand_add_text, randomCar.brand)
        click_text_input_year_production_and_choose_year(R.id.car_year_add_text, 2016)
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
        //TODO check current location
        //TODO check if car was added
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
