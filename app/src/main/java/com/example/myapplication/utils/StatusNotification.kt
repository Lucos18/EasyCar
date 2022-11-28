package com.example.myapplication.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R
import com.example.myapplication.workers.*

fun makeStatusNotification(message: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))


/*
var builder = NotificationCompat.Builder(context, CHANNEL_ID)
    // Show controls on lock screen even when user hides sensitive content.
    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
    // Add media control buttons that invoke intents in your media service // #2
    // Apply the media style template
    .setStyle(MediaNotificationCompat.MediaStyle()
        .setShowActionsInCompactView(1 /* #1: pause button \*/)
        .setMediaSession(mediaSession.getSessionToken()))
    .setContentTitle("Wonderful music")
    .setContentText("My Awesome Band")
    .setLargeIcon()
    .build()


 */
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}