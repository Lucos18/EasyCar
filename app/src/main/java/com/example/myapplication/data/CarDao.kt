package com.example.myapplication.data

import androidx.room.*
import com.example.myapplication.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM Car")
    fun getCars(): Flow<List<Car>>

    @Query("SELECT * FROM Car WHERE id = :id")
    fun getCarById(id: Long): Flow<Car>

    @Query("DELETE FROM Car WHERE id = :id")
    suspend fun deleteCarById(id: Long)

    @Query("SELECT COUNT(*) FROM Car WHERE favorite = 1")
    fun getFavoritesCarNumber():Flow<Int>

    @Query("SELECT * FROM Car WHERE favorite = 1")
    fun getAllFavoritesCar():Flow<List<Car>>

    @Query("SELECT * FROM Car WHERE favorite = 1 ORDER BY mileage ASC")
    fun getAllFavoritesCarMileageAscending():Flow<List<Car>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: Car)

    @Update
    fun update(car: Car)

    @Delete
    suspend fun delete(car: Car)


}