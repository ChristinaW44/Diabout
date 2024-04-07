package com.example.diabout.helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class WeekValueFormatter() : ValueFormatter() {

    private val month =
        arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return month.getOrNull(value.toInt()) ?: value.toString()
    }
}