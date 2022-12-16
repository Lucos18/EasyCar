package com.example.myapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.favorites.FavoritesFragmentDirections
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeFragmentDirections
import com.example.myapplication.ui.search.SearchFragmentDirections
import com.example.myapplication.ui.sell.SellFragmentDirections
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class NavigationInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val mockNavController = mock(NavController::class.java)
    val testNavController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )
    val theme = R.style.Theme_MyApplication

    @Test
    fun navigate_between_fragments_by_using_navigation_view_and_reverse() {
        val navigationItems = setOf(
            R.id.navigation_home,
            R.id.navigation_search,
            R.id.navigation_favorites,
            R.id.navigation_sell
        )
        navigationItems.forEach {
            onView(withId(it))
                .perform(click())
            //TODO Check if current location is expected with nav controller
            //check_current_location_is_expected(navController.currentDestination?.id, it)
        }
        navigationItems.reversed().forEach {
            onView(withId(it))
                .perform(click())
            //TODO Check if current location is expected with nav controller
            //check_current_location_is_expected(navController.currentDestination?.id, it)
        }
    }

    @Test
    fun test_navigation_from_home_fragment_to_search_fragment() {
        val homeFragmentScenario =
            launchFragmentInContainer<HomeFragment>(themeResId = theme)
        homeFragmentScenario.onFragment { fragment ->
            testNavController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), testNavController)
        }
        simple_click(R.id.search_cars)
        check_current_location_is_expected(
            testNavController.currentDestination?.id,
            R.id.navigation_search
        )
    }

    @Test
    fun test_navigation_from_home_fragment_to_detail_car_fragment() {
        val mockNavController = startHomeFragment()
        click_on_card(0)
        verify(mockNavController).navigate(
            HomeFragmentDirections.actionNavigationHomeToDetailCarFragment(
                1,
                false
            )
        )
    }

    @Test
    fun test_navigation_from_search_fragment_to_search_results_fragment() {
        val mockNavController = startSearchFragment()
        onView(withId(R.id.search_cars_button)).check(matches(isDisplayed()))
        simple_click(R.id.search_cars_button)
        verify(mockNavController).navigate(SearchFragmentDirections.actionNavigationSearchToSearchResults())
    }

    @Test
    fun test_navigation_from_favorites_fragment_to_detail_car_fragment() {
        val mockNavController = startFavoritesFragment()
        click_on_card(0)
        verify(mockNavController).navigate(
            FavoritesFragmentDirections.actionNavigationFavoritesToDetailCarFragment(
                1,
                false
            )
        )
    }

    @Test
    fun test_navigation_from_sell_fragment_to_add_new_car_fragment() {
        val mockNavController = startSellFragment()
        onView(withId(R.id.add_car_fab)).check(matches(isDisplayed()))
        simple_click(R.id.add_car_fab)
        verify(mockNavController).navigate(SellFragmentDirections.actionNavigationSellToAddNewCarFragment())
    }

    @Test
    fun test_navigation_from_sell_fragment_to_detail_car_fragment() {
        val mockNavController = startSellFragment()
        click_on_card(0)
        verify(mockNavController).navigate(
            SellFragmentDirections.actionNavigationSellToDetailCarFragment(
                1,
                true
            )
        )
    }
}