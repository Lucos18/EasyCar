package com.example.myapplication.utils

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.model.Car
import com.google.android.material.snackbar.Snackbar

fun showCustomSnackBar(constraintLayout: ConstraintLayout, text: String, length: Int) {
    val snackbar = Snackbar
        .make(
            constraintLayout,
            text,
            length
        )
    snackbar.show()
}

fun showCustomSnackBarWithUndo(
    constraintLayout: ConstraintLayout,
    textOnClick: String,
    length: Int,
    textOnUndo: String,
    functionUndo: () -> Unit
) {
    val snackbar = Snackbar.make(
        constraintLayout,
        textOnClick,
        length
    ).setAction(textOnUndo){
        functionUndo()
    }
    snackbar.show()
}


