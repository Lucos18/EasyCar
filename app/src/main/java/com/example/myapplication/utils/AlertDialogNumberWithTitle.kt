package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
fun AlertDialogNumberWithTitle(layoutInflater: LayoutInflater, context: Context,title: Int, TextInputBinding: TextInputEditText, lengthFilter: Int){
    val builder = AlertDialog.Builder(context)
        .create()
    val view = layoutInflater.inflate(R.layout.alert_dialog_input_text_price, null)
    val searchText = view.findViewById<TextView>(R.id.price_title_alert_dialog)
    val carPowerText =
        view.findViewById<TextInputEditText>(R.id.car_price_alert_dialog_text)
    carPowerText.filters += InputFilter.LengthFilter(lengthFilter)
    val btnOk = view.findViewById(R.id.button_ok) as Button
    val btnCancel = view.findViewById(R.id.button_cancel) as Button
    searchText.setText(title)
    btnOk.setOnClickListener {
        TextInputBinding.text = carPowerText.text
        builder.dismiss()
    }
    btnCancel.setOnClickListener {
        TextInputBinding.setText("")
        builder.dismiss()
    }
    builder.setCanceledOnTouchOutside(true)
    builder.setView(view)
    builder.show()
    builder.window?.setLayout(900, WindowManager.LayoutParams.WRAP_CONTENT)
}
