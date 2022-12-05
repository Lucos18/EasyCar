package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText

fun carListItemsAlertDialog(context: Context, layoutInflater: LayoutInflater, listItem: List<String>, textInputEditText: TextInputEditText, textInputEditTextToEnableOnClick: TextInputEditText?){
    lateinit var adapter: ArrayAdapter<*>
    val builder = AlertDialog.Builder(context)
        .create()
    val view = layoutInflater.inflate(R.layout.alert_dialog, null)
    val searchText = view.findViewById<SearchView>(R.id.search_view)
    val listViewBrand = view.findViewById<ListView>(R.id.listView)
    adapter = ArrayAdapter(
        context,
        android.R.layout.simple_list_item_1,
        listItem
    )
    listViewBrand.adapter = adapter
    listViewBrand.onItemClickListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            textInputEditText.setText(
                listViewBrand.getItemAtPosition(position).toString()
            )
            if (textInputEditTextToEnableOnClick != null){
                textInputEditTextToEnableOnClick.setText("")
                textInputEditTextToEnableOnClick.isEnabled = true
            }
            builder.dismiss()
        }
    listViewBrand.emptyView = view.findViewById(R.id.empty_text_view_search)
    searchText.setOnClickListener {
        searchText.isIconified = false
    }
    searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            adapter.filter.filter(p0)
            return true
        }
    })
    builder.setCanceledOnTouchOutside(true)
    builder.setView(view)
    builder.show()
}