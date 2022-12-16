package com.example.myapplication

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.CarColors
import com.example.myapplication.enums.fuelType
import com.example.myapplication.model.Car
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class HomeFragmentInstrumentedTest {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()
    val carDao = database.CarDao()

    //TODO To fix need to check what car needed to be pressed the favorites button

    @Test
    fun click_add_to_favorites_car() {

        go_to_home_fragment()
        click_on_favorites_button(0)

        //TODO Check if favorites has changed on that id
    }

    @Test
    fun test()  = runBlocking {
        val Car = Car(
            brand = "ALFA ROMEO",
            model = "Giulia",
            yearStartProduction = 2016,
            yearEndProduction = null,
            carPower = 150,
            seats = 5,
            fuelType = fuelType.Diesel.toString(),
            image = null,
            color = CarColors.MATTE_BLUE.nameColor,
            mileage = 0.0,
            price = 100000.0
        )
        carDao.insert(Car)
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            carDao.getCars().collect {
            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}