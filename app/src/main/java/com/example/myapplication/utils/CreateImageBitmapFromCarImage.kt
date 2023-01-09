package com.example.myapplication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.myapplication.workers.CarReminderWorker.Companion.carId
import com.example.myapplication.workers.CarReminderWorker.Companion.carImage
import com.example.myapplication.workers.FORMAT_IMAGE_PNG
import com.example.myapplication.workers.IMAGE_NAME
import java.io.File
import java.io.FileOutputStream

fun createBitmapFromCarImage(carImage: ByteArray, carId: Long): String{
    val bitmap = BitmapFactory.decodeByteArray(carImage, 0, carImage.size)
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
    return path
}

fun checkIfInsertIsNull(image: Bitmap, imageView: ImageView): Bitmap? {
    return if (imageView.tag == "is_not_null") {
        image
    } else {
        null
    }
}

fun createBitmapFromView(view: View): Bitmap {
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    return view.drawingCache
}