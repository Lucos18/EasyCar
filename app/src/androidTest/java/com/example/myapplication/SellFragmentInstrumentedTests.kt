package com.example.myapplication

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.CarDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SellFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()
    val carDao = database.CarDao()

    @Test
    fun click_card_and_modify_current_values() {
        go_to_sell_fragment()
        click_on_card_and_check_details_card(0)
        click_edit_car_fab()
        clear_text_text_input(R.id.car_power_edit_text)
        click_text_input_and_write_string(R.id.car_power_edit_text, "150")
        hideKeyboard()
        click_confirm_edit_car_fab()
    }

    @Test
    fun click_card_and_delete_car() {
        go_to_sell_fragment()
        click_on_card_and_check_details_card(0)
        click_delete_car_fab()
    }
}