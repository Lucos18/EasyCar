package com.example.myapplication

import android.app.PendingIntent.getActivity
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.enums.fuelType
import com.example.myapplication.utils.FuelTypeAlertDialog
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.junit.Assert


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

fun navigate_to_sell_fragment_and_add_new_car() {
    go_to_sell_fragment()
    //TODO Add check if the click went well by checking if the user is in that fragment
    click_add_new_car_fab()
}

fun check_current_location_is_expected(currentLocation: Int?, expectedLocation: Int) {
    //assert(currentLocation.toString()).isEqualTo(expectedLocation)
}

fun simple_click(idTextInput: Int){
    onView(withId(idTextInput))
        .perform(click())
}
fun click_on_card_and_check_details_card(idCard: Int) {
    click_on_card(idCard)
    //TODO Check if navigation went well
}

fun click_add_new_car_fab() {
    onView(withId(R.id.add_car_fab))
        .perform(click())
}

fun click_text_input_and_write_string(idTextInput: Int, stringToWrite: String) {
    onView(withId(idTextInput))
        .perform(typeText(stringToWrite))
}

fun click_text_input_brand_and_choose_by_position(idTextInput: Int, positionItemClick: Int) {
    Thread.sleep(2000)
    simple_click(idTextInput)
    Thread.sleep(2000)
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionItemClick).perform(click());
}
fun click_text_input_year_production_and_choose_year(idTextInput: Int, year: Int){
    simple_click(idTextInput)
    onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
        PickerActions.setDate(
            year,
            1,
            1
        )
    )
    click_positive_button_alert_dialog()
}
fun click_text_input_model_and_choose_by_position(idTextInput: Int, positionItemClick: Int){
    simple_click(idTextInput)
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionItemClick).perform(click());
}
fun click_text_input_fuel_and_choose_by_position(idTextInput: Int, positionItemClick: Int){
    simple_click(idTextInput)
    onView(withText(fuelType.Diesel.toString())).perform(click())
    click_positive_button_alert_dialog()
}
fun click_text_input_color_and_choose_by_position(idTextInput: Int, positionItemClick: Int){
    simple_click(idTextInput)
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionItemClick).perform(click());
}
//TODO FIX not found text
fun click_text_input_brand_and_choose_by_text(idTextInput: Int, positionItemClick: Int) {
    Thread.sleep(2000)
    simple_click(idTextInput)
    Thread.sleep(2000)
    onData(anything()).inAdapterView(withId(R.id.listView)).onChildView(allOf(withText("ALFA ROMEO"))).perform(click());
}

fun click_on_card(idCard: Int) {
    onView(withId(R.id.recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(idCard, click())
    )
}

fun click_on_favorites_button(idCard: Int) {
    onView(withId(R.id.recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(idCard, clickOnViewChild(R.id.favorites_button_image))
    )
}

fun click_edit_car_fab() {
    onView(withId(R.id.edit_car_fab))
        .perform(click())
}

fun click_confirm_edit_car_fab() {
    onView(withId(R.id.save_car_fab))
        .perform(click())
}

fun click_delete_car_fab(){
    onView(withId(R.id.delete_car_fab))
        .perform(click())
}

fun clear_text_text_input(idTextInput: Int) {
    onView(withId(idTextInput)).perform(clearText())
}

fun hideKeyboard() {
    onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard())
}

fun click_positive_button_alert_dialog(){
    onView(withId(android.R.id.button1)).perform(click())
}

fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
}