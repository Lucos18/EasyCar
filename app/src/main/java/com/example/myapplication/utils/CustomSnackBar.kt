package com.example.myapplication.utils

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar

fun showCustomSnackBar(constraintLayout: ConstraintLayout, text: String, length: Int) {
    val snackbar = com.google.android.material.snackbar.Snackbar
        .make(
            constraintLayout,
            text,
            length
        )
    snackbar.show()
}

fun showCustomSnackBarWithUndo(constraintLayout: ConstraintLayout, textOnClick: String, length: Int, textOnUndo:String) {
    val snackbar = Snackbar.make(
        constraintLayout,
        textOnClick,
        length
    )
    snackbar.setAction(textOnUndo, MyUndoListener())
    snackbar.show()
}
class MyUndoListener : View.OnClickListener {
    override fun onClick(v: View) {
        Log.d("testsnack", "testsnackundo")
    }
}

