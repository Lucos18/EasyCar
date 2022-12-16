package com.example.myapplication

import android.app.Activity
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.model.Car
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SellFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()
    val randomCar = CarList.random()
    val carDao = database.CarDao()
    private lateinit var uiDevice: UiDevice

    @Before
    fun setup(){
        uiDevice = UiDevice.getInstance(getInstrumentation())

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun click_card_and_modify_current_values() {
        go_to_sell_fragment()
        click_on_card_and_check_details_card(0)
        click_edit_car_fab()
        clear_text_text_input(R.id.car_power_edit_text)
        click_text_input_and_write_string(R.id.car_power_edit_text, "150")
        clear_text_text_input(R.id.car_price_edit_text)
        click_text_input_and_write_string(R.id.car_price_edit_text, "10000")
        clear_text_text_input(R.id.car_seats_edit_text)
        click_text_input_and_write_string(R.id.car_seats_edit_text, "5")
        click_text_input_fuel_and_choose_by_position(R.id.car_fuel_type_edit_label, 4)
        click_text_input_year_production_and_choose_year(R.id.car_year_production_edit_text, 2015)
        scrollTo(R.id.car_mileage_text_edit)
        clear_text_text_input(R.id.car_mileage_text_edit)
        click_text_input_and_write_string(R.id.car_mileage_text_edit, "180000")
        hideKeyboard()
        click_confirm_edit_car_fab()
    }

    @Test
    fun click_card_and_delete_car() {
        go_to_sell_fragment()
        click_on_card_and_check_details_card(0)
        click_delete_car_fab()
    }

    @Test
    fun click_card_and_modify_mileage_for_notification(){
        go_to_sell_fragment()
        click_on_card_and_check_details_card(0)
        click_edit_car_fab()
        //TODO get current mileage to update after
        clear_text_text_input(R.id.car_mileage_text_edit)
        click_text_input_and_write_string(R.id.car_mileage_text_edit, "1000000")
        hideKeyboard()
        click_confirm_edit_car_fab()
        uiDevice.pressHome()
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textContains("ALFA ROMEO")), 20)
    }
}