package com.noatnoat.todoapp.view.adapter

import java.text.SimpleDateFormat
import java.util.Date
import androidx.databinding.BindingAdapter
import android.widget.TextView

class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("android:text")
        fun convertLongToDateString(view: TextView, timestamp: Long) {
            val sdf = SimpleDateFormat("dd/MM/yy")
            val date = Date(timestamp)
            view.text = sdf.format(date)
        }
    }
}