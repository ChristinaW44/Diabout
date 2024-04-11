package com.example.diabout.helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class WeekValueFormatter() : ValueFormatter() {

    private val month =
        arrayOf("M", "T", "W", "T", "F", "S", "S")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return month.getOrNull(value.toInt()) ?: value.toString()
    }
}