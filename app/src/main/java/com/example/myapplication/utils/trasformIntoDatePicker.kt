package com.example.myapplication.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

fun EditText.transformIntoDatePicker(
    context: Context,
    format: String,
    maxDate: Date? = null
) {
    //val maxYearCar = addNewCarViewModel.getDistinctMaxYearCarByBrand(binding.carBrandAddText.text.toString())
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, _ ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            val sdf = SimpleDateFormat(format, Locale.ITALY)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context, datePickerOnDataSetListener,
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH),
        ).run {
            maxDate?.time?.also { datePicker.maxDate = it }
            show()
        }
    }
}