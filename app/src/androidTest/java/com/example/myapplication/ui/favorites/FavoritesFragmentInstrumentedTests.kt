package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.myapplication.data.CarDao
import com.example.myapplication.data.CarDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesFragmentInstrumentedTest {
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

    //TODO CHECK TEST
    @Test
    fun get_favorites_number_and_check_number_on_database() = runBlocking {
        startFavoritesFragment()
        var numberOfCars: Int = 0
        var numberOfCarsFavorites: Int = 0
        dao.insert(CarListDao[0])
        dao.insert(CarListDao[1])
        dao.insert(CarListDao[3])
        dao.getCars().test {
            val list = awaitItem()
            numberOfCars = list.size
            cancel()
        }
        dao.getFavoritesCarNumber().test {
            val list = awaitItem()
            numberOfCarsFavorites = list
            cancel()
        }
        assert(numberOfCars != numberOfCarsFavorites)
    }
}
