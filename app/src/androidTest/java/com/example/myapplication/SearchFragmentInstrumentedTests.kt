package com.example.myapplication

import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.CarDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFragmentInstrumentedTests {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun show_error_when_no_car_found() {
        go_to_search_fragment()
        click_text_input_and_write_string(R.id.ending_price_search_text, "0")
        simple_click(R.id.search_cars_button)
        onView(withId(R.id.image_error_no_car_found)).check(ViewAssertions.matches(isDisplayed()))
    }

}