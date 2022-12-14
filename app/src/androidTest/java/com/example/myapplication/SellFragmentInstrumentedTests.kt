package com.example.myapplication

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SellFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigate_to_sell_fragment_and_add_new_car(){
        go_to_sell_fragment()
        //TODO Add check if the click went well by checking if the user is in that fragment
        click_add_new_car_fab()

        click_text_input_and_write_string(R.id.car_power_add_text, "150")

    }
    fun click_card_and_navigate_to_detail_fragment(){
        go_to_sell_fragment()
        click_on_card(0)
    }
}