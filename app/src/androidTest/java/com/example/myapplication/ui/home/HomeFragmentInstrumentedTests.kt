package com.example.myapplication

import android.R
import android.app.PendingIntent.getActivity
import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()

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
    //TODO FIX

    @Test
    fun click_add_to_favorites_car() = runBlocking {
        go_to_home_fragment()
        var initialNumberOfFavorites = 0
        var endingNumberOfFavorites = 0
        dao.insert(CarListDao[0])
        dao.getAllFavoritesCar().test {
            val list = awaitItem()
            initialNumberOfFavorites = list.size
            cancel()
        }
        click_on_favorites_button(0)
        dao.getAllFavoritesCar().test {
            val list = awaitItem()
            endingNumberOfFavorites = list.size
            cancel()
        }
        assert(initialNumberOfFavorites != endingNumberOfFavorites)
    }
}