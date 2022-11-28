package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Car

@Database(entities = [Car::class], version = 10, exportSchema = false)
abstract class CarDatabase: RoomDatabase() {
    abstract fun CarDao(): CarDao
    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null
        fun getDatabase(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}