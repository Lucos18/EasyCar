package com.example.myapplication.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.MainActivity
import com.example.myapplication.enums.fuelType
import com.example.myapplication.model.Car
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CarDatabaseTest {
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()
    val carTest = Car(
        id = 199,
        brand = "CITROEN",
        model = "C3",
        yearStartProduction = 2016,
        yearEndProduction = null,
        carPower = 160,
        seats = 2,
        fuelType = fuelType.PHEV.toString(),
        image = null,
        color = "test",
        mileage = 0.0,
        price = 40000.0
    )

    @BeforeEach
    fun setUp() {

        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
    }

    @After
    fun closeDb() {
        db.close()
    }


    @org.junit.jupiter.api.Test
    fun carDao() = runBlocking {
        val car = carTest
        dao.insert(car)
        val carSearch = dao.getCarById(199)
        assertThat(carSearch, equalTo(car))
    }
}
