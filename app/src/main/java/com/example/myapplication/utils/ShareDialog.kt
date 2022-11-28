package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import javax.security.auth.Subject

fun ShareDialog(context: Context, subject: String, extratext: String, shareWith: String){
    val context = context

    val type = "text/plain"
    val subject = subject
    val extraText = extratext
    val shareWith = shareWith

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, extraText)

    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, shareWith),
        null
    )
}