package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import java.io.File

fun ShareDialog(context: Context, subject: String, extratext: String, shareWith: String, FileUri: File?){
    val context = context
    val type = "text/plain"
    val subject = subject
    val extraText = extratext
    val shareWith = shareWith

    val intent = Intent(Intent.ACTION_SEND)
    if (FileUri != null)
    {
        intent.type = "image/*";
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context,"com.example.myapplication.provider",FileUri));
    } else intent.type = "text/plain"

    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, extraText)

    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, shareWith),
        null
    )
    //deleteImage(FileUri)
}
fun deleteImage(path: File?) {
    path?.delete()
}