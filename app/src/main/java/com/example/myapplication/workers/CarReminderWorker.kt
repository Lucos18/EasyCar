package com.example.myapplication.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.BaseApplication
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import java.io.File


class CarReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    // Arbitrary id number
    val notificationId = 17

    override fun doWork(): Result {

        //Get values in input from CarWorkerViewModel
        val carModel = inputData.getString(carModel)
        val carBrand = inputData.getString(carBrand)
        val carIdLong = inputData.getLong(carId, 0)
        val title = inputData.getString(title)
        val content = inputData.getString(content)
        val path = inputData.getString(carImage)
        val imagePath = "$path/$IMAGE_NAME$carIdLong$FORMAT_IMAGE_PNG"

        //Get Bitmap from locally saved image
        val myBitmap = BitmapFactory.decodeFile(imagePath)
        //then deletes it from the gallery
        deleteImage(imagePath)
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
            putExtra("ID", carIdLong)
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            .setContentTitle("$title")
            .setContentText("$content")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setLargeIcon(myBitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(myBitmap)
                    .bigLargeIcon(null)
            )
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
        return Result.success()
    }

    companion object {
        const val carBrand = "BRAND"
        const val carId = "ID"
        const val carModel = "MODEL"
        const val title = "TITLE"
        const val content = "CONTENT"
        const val carImage = "IMAGE"
    }

    fun deleteImage(path: String) {
        val fDelete = File(path)
        fDelete.delete()
    }
}
