package com.example.myapplication

import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4


fun go_to_sell_fragment() {
    onView(withId(R.id.navigation_sell))
        .perform(click())
}

fun go_to_home_fragment() {
    onView(withId(R.id.navigation_home))
        .perform(click())
}

fun go_to_search_fragment() {
    onView(withId(R.id.navigation_search))
        .perform(click())
}

fun go_to_favorites_fragment() {
    onView(withId(R.id.navigation_favorites))
        .perform(click())
}

fun check_current_location_is_expected(currentLocation: Int?, expectedLocation: Int){

}

fun click_add_new_car_fab() {
    onView(withId(R.id.add_car_fab))
        .perform(click())
}

fun click_text_input_and_write_string(idTextInput: Int, stringToWrite: String){
    onView(withId(idTextInput))
        .perform(typeText(stringToWrite))
}

fun click_text_input_brand_and_choose(idTextInput: Int, positionItemClick: Int){
    onView(withId(idTextInput))
        .perform(click())
}

fun click_on_card(idCard: Int){
    onView(withId(R.id.recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(idCard,click()))
}