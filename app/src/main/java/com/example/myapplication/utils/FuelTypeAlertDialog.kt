package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.myapplication.model.fuelType
import com.google.android.material.textfield.TextInputEditText

fun FuelTypeAlertDialog(context: Context,text: TextInputEditText){
    val values: Array<fuelType> = fuelType.values()
    val items = arrayOfNulls<CharSequence>(values.size)
    for (i in values.indices) {
        items[i] = values[i].toString()
    }
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setTitle("Choose fuel type")
    builder.setSingleChoiceItems(items, -1) { dialogInterface: DialogInterface, which ->
        text.setText(items[which].toString())
    }
    builder.setItems(items) { _: DialogInterface, which ->
        text.setText(items[which].toString())
    }
    builder.setPositiveButton("OK") { _: DialogInterface, _ ->

    }
    builder.setNegativeButton("Cancel") { _: DialogInterface, _ ->
        text.setText("")
    }
    builder.show()
}