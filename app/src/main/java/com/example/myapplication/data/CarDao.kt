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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: Car)

    @Update
    fun update(car: Car)

    @Delete
    suspend fun delete(car: Car)
}