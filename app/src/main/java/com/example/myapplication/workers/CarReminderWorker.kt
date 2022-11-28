package com.example.myapplication.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.BaseApplication
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class CarReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    // Arbitrary id number
    val notificationId = 17

    override fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)

        val carModel = inputData.getString(carModel)
        val carBrand = inputData.getString(carBrand)
        val carId = inputData.getLong(carId, 0)
        val title = inputData.getString(title)
        val content = inputData.getString(content)

        val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            .setContentTitle("$title $carBrand")
            .setContentText("$content $carModel")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
                /*
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(R.drawable.ic_baseline_add_24)
                .bigLargeIcon(null))

                 */
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
}
