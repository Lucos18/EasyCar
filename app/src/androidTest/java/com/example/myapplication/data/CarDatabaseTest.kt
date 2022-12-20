package com.example.myapplication.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.myapplication.CarListDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CarDatabaseTest {
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

    @Test
    fun addCar_shouldReturn_theItem_inFlow() = runBlocking {
        val car = CarListDao[0]
        dao.insert(car)
        dao.getCars().test {
            val list = awaitItem()
            assert(list.contains(car))
            cancel()
        }
    }

    @Test
    fun deletedCar_shouldNot_be_present_inFlow() = runBlocking {
        val car = CarListDao[0]
        val car1 = CarListDao[1]
        dao.insert(car)
        dao.insert(car1)
        dao.delete(car)
        dao.getCars().test {
            val list = awaitItem()
            assert(list.size == 1)
            assert(list.contains(car1))
            cancel()
        }
    }

    @Test
    fun updateCar_shouldReturn_theItem_inFlow() = runBlocking {
        val carRandom1 = CarListDao[0]
        val carRandom2 = CarListDao[1]
        val carRandom3 = CarListDao[2]
        dao.insert(carRandom1)
        dao.insert(carRandom2)
        dao.update(carRandom3)
        dao.getCars().test {
            val list = awaitItem()
            assert(list.size == 2)
            assert(list.contains(carRandom3))
            cancel()
        }
    }

    @Test
    fun deleteCarById_shouldNot_be_present_inFlow() = runBlocking {
        val carRandom1 = CarListDao[0]
        val carRandom2 = CarListDao[1]
        dao.insert(carRandom1)
        dao.insert(carRandom2)
        dao.deleteCarById(carRandom1.id)
        dao.getCars().test {
            val list = awaitItem()
            assert(list.size == 1)
            assert(list.contains(carRandom2))
            cancel()
        }
    }

    @Test
    fun getCarById_shouldBe_present_inFlow() = runBlocking {
        val carRandom1 = CarListDao[0]
        val carRandom2 = CarListDao[1]
        dao.insert(carRandom1)
        dao.insert(carRandom2)
        dao.getCarById(carRandom1.id).test {
            val list = awaitItem()
            assert(list == carRandom1)
            cancel()
        }
    }

    @Test
    fun getAllFavoritesCar_shouldBe_present_inFlow() = runBlocking {
        val carRandom1 = CarListDao[0]
        val carRandom2 = CarListDao[1]
        val carRandom3 = CarListDao[3]
        dao.insert(carRandom1)
        dao.insert(carRandom2)
        dao.insert(carRandom3)
        dao.getAllFavoritesCar().test {
            val list = awaitItem()
            assert(list.size == 2)
            assert(list.contains(carRandom2))
            assert(list.contains(carRandom3))
            cancel()
        }
    }

    @Test
    fun getFavoriteCarById_shouldBe_be_present_inFlow() = runBlocking {
        val carRandom1 = CarListDao[0]
        val carRandom3 = CarListDao[3]
        dao.insert(carRandom1)
        dao.insert(carRandom3)
        dao.getFavoritesCarNumber().test {
            val list = awaitItem()
            assert(list == 1)
            cancel()
        }

    }
}
