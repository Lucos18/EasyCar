package com.example.myapplication.ui.search

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.ui.searchResults.SearchResultsDirections
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class SearchFragmentInstrumentedTests {
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)


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
    fun show_error_when_no_car_found(): Unit = runBlocking {
        go_to_search_fragment()
        click_text_input_and_write_string(R.id.ending_price_search_text, "1")
        scrollTo(R.id.new_car_button)
        simple_click(R.id.new_car_button)
        hideKeyboard()
        simple_click(R.id.search_cars_button)
        onView(withId(R.id.image_error_no_car_found)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun search_car_with_all_parameters(): Unit = runBlocking {
        go_to_search_fragment()
        val carToInsert = CarListDao[2]
        dao.insert(carToInsert)
        click_text_input_and_write_string(R.id.starting_price_search_text, (carToInsert.price - 100).toString())
        click_text_input_and_write_string(R.id.ending_price_search_text, (carToInsert.price + 100).toString())
        hideKeyboard()
        scrollTo(R.id.new_car_button)
        simple_click(R.id.new_car_button)
        simple_click(R.id.diesel_filter)
        simple_click(R.id.gas_filter)
        simple_click(R.id.CV_filter)
        scrollTo(R.id.checkbox_at_least_one_photo)
        click_text_input_and_write_string(R.id.car_search_power_starting_text, (carToInsert.carPower - 15).toString())
        click_text_input_and_write_string(R.id.car_search_power_ending_text, (carToInsert.carPower + 15).toString())
        hideKeyboard()
        Thread.sleep(40000)
        click_text_input_list_view_and_choose_by_text(R.id.car_search_brand_text, carToInsert.brand)
        click_text_input_list_view_and_choose_by_text(R.id.car_search_model_text, carToInsert.model)
        hideKeyboard()
        simple_click(R.id.search_cars_button)
        Mockito.verify(mockNavController).navigate(
            SearchFragmentDirections.actionNavigationSearchToSearchResults())
        click_on_card(0)
        Mockito.verify(mockNavController).navigate(
            SearchResultsDirections.actionSearchResultsToDetailCarFragment(1,false))

    }
}