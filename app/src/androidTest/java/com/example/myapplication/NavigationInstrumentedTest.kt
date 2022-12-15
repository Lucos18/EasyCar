package com.example.myapplication

import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.dx.command.Main
import com.example.myapplication.ui.home.HomeFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class NavigationInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Test
    fun navigate_between_fragments_by_using_navigation_view_and_reverse() {
        val navigationItems = setOf(
            R.id.navigation_home,
            R.id.navigation_search,
            R.id.navigation_favorites,
            R.id.navigation_sell
        )
        navigationItems.forEach{
            onView(withId(it))
                .perform(click())
            Log.d("ciao", navController.currentDestination?.id.toString() )
            //TODO Check if current location is expected with nav controller
           // check_current_location_is_expected(navController.currentDestination?.id, it)
        }
        navigationItems.reversed().forEach{
            onView(withId(it))
                .perform(click())
            //TODO Check if current location is expected with nav controller
           // check_current_location_is_expected(navController.currentDestination?.id, it)
        }
    }
}