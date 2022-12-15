package com.example.myapplication.workers

import android.app.Application
import android.app.slice.SliceItem.FORMAT_IMAGE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.myapplication.utils.createBitmapFromCarImage
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit


class CarWorkerViewModel(val application: Application) : ViewModel() {
    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        title: String,
        content: String,
        carId: Long,
        carBrand: String,
        carModel: String,
        carImage: ByteArray?

    ) {
        val path = carImage?.let { createBitmapFromCarImage(it, carId) }
        val dataToWorker: Data =
            workDataOf(
                CarReminderWorker.title to title,
                CarReminderWorker.content to content,
                CarReminderWorker.carId to carId,
                CarReminderWorker.carBrand to carBrand,
                CarReminderWorker.carModel to carModel,
                CarReminderWorker.carImage to path
            )

        val carRequest = OneTimeWorkRequestBuilder<CarReminderWorker>()
            .setInputData(dataToWorker)
            .setInitialDelay(duration, unit)
            .build()

        WorkManager
            .getInstance(application)
            .enqueueUniqueWork(carId.toString(), ExistingWorkPolicy.REPLACE, carRequest)
    }
}


class CarWorkerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CarWorkerViewModel::class.java)) {
            CarWorkerViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
