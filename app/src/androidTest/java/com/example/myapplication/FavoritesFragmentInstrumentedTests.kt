package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.CarDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()
    val carDao = database.CarDao()


    @Test
    fun get_favorites_number_and_check_number_on_database() {
        startFavoritesFragment()
        val favoritesNumber = carDao.getFavoritesCarNumber()
    }
}
