package com.sessac.travel_agency.common

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class CommonHandler{
    fun spinnerHandler(items: Array<String>, binding: AutoCompleteTextView, context: Context) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        val autoCompleteTextView: AutoCompleteTextView = binding
        autoCompleteTextView.setAdapter(adapter)
    }

    fun dateHandler(firstDate: Array<String>, secondDate: AutoCompleteTextView, context: Context) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    }
}