package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.enums.CarFiltersFavourites
import com.example.myapplication.enums.fuelType
import com.google.android.material.textfield.TextInputEditText

var checkedItemFavourites: Int = -1
fun FavouritesFilterAlertDialog(context: Context, applyFavoritesFilter: () -> Unit){
    val values: Array<CarFiltersFavourites> = CarFiltersFavourites.values()
    val items = arrayOfNulls<CharSequence>(values.size)
    for (i in values.indices) {
        items[i] = values[i].nameFilter
    }
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.choose_current_filter_favorites_alert_dialog_text))
    builder.setSingleChoiceItems(items, checkedItemFavourites) { _: DialogInterface, which ->
        //text.setText(items[which].toString())
    }
    builder.setItems(items) { _: DialogInterface, which ->
        checkedItemFavourites = which
        //text.setText(items[which].toString())
    }
    builder.setPositiveButton(context.getString(R.string.delete_car_dialog_positive_button)) { _: DialogInterface, which ->
        CarFiltersFavourites.values().forEach { it.isSelected = false }
        CarFiltersFavourites.valueOf(values[checkedItemFavourites].toString()).isSelected = true
        applyFavoritesFilter()
    }
    builder.setNegativeButton(context.getString(R.string.delete_car_dialog_negative_button)) { _: DialogInterface, _ ->
        checkedItemFavourites = -1
    }
    builder.show()
}