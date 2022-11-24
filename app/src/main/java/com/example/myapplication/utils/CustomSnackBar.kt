package com.example.myapplication.utils

import androidx.constraintlayout.widget.ConstraintLayout

fun showCustomSnackBar(constraintLayout: ConstraintLayout, text: String, length: Int) {
    val snackbar = com.google.android.material.snackbar.Snackbar
        .make(
            constraintLayout,
            text,
            length
        )
    snackbar.show()
}