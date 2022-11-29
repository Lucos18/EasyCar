package com.example.myapplication.workers

import android.app.Application
import android.app.slice.SliceItem.FORMAT_IMAGE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
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
        val bitmap = carImage?.let { BitmapFactory.decodeByteArray(carImage, 0, it.size) }
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        try {
            val file = File(path, "$IMAGE_NAME$carId$FORMAT_IMAGE_PNG")
            val fOut = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
