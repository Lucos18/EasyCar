package com.example.myapplication.workers

import android.app.Application
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.icu.text.CaseMap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.myapplication.workers.CarReminderWorker.Companion.title
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


class CarWorkerViewModel(val application: Application) : ViewModel() {
    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        title: String,
        content: String,
        carId: Long,
        carBrand: String,
        carModel:String,
        carImage: ByteArray?

    ) {
//TODO create some black magic to store the image internally so it can be passed
        val bitmap = carImage?.let { BitmapFactory.decodeByteArray(carImage, 0, it.size) }

        val mydata: Data =
            workDataOf(
                CarReminderWorker.title to title,
                CarReminderWorker.content to content,
                CarReminderWorker.carId to carId,
                CarReminderWorker.carBrand to carBrand,
                CarReminderWorker.carModel to carModel,
                //CarReminderWorker.carImage to byteArrayOf(carImage)
            )

        val carRequest = OneTimeWorkRequestBuilder<CarReminderWorker>()
            .setInputData(mydata)
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
