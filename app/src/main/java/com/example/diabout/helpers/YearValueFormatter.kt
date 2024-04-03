package com.example.diabout.helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class YearValueFormatter() : ValueFormatter()  {

    private val month = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep","Oct","Nov","Dec")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return month.getOrNull(value.toInt()) ?: value.toString()
    }
}