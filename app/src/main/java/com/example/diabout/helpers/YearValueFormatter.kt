package com.example.diabout.helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class YearValueFormatter() : ValueFormatter()  {
    //used to format the yearly bar chart labels
    private val month = arrayOf("J","F","M","A","M","J","J","A","S","O","N","D")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return month.getOrNull(value.toInt()) ?: value.toString()
    }
}