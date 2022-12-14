package com.example.myapplication

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.CarDatabase
import com.example.myapplication.enums.CarColors
import com.example.myapplication.enums.fuelType
import com.example.myapplication.model.Car
import junit.runner.Version.id
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

class DaoUnitTest {
    @get:Rule()
    val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarDatabase::class.java
    ).allowMainThreadQueries().build()
    val carDao = database.CarDao()
    //TODO To fix need to check what car needed to be pressed the favorites button


}
