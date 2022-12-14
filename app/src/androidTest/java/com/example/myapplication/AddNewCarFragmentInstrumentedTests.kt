package com.example.myapplication

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddNewCarFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun go_to_add_new_car_fragment_and_add_new_car(){
        go_to_sell_fragment()
        click_add_new_car_fab()
        //TODO check current fragment
        click_text_input_brand_and_choose_by_position(R.id.car_brand_add_text, 1)
        click_text_input_year_production_and_choose_year(R.id.car_year_add_text, 2016)
        click_text_input_model_and_choose_by_position(R.id.car_model_add_text, 0)
        //TODO add fuel type choose by position
        click_text_input_and_write_string(R.id.car_power_add_text,"150")
        click_text_input_and_write_string(R.id.car_seats_add_text,"5")
        click_text_input_and_write_string(R.id.car_price_add_text,"500000")
        click_text_input_and_write_string(R.id.car_mileage_add_text,"0")
        click_text_input_color_and_choose_by_position(R.id.car_color_add_text, 0)
        Thread.sleep(5000)
        //TODO add image somewhat
        simple_click(R.id.button_add_new_car)
        //TODO check current location
        //TODO check if car was added
    }
}
