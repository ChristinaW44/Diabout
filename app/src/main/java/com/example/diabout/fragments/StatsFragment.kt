package com.example.diabout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.MonthValueFormatter
import com.example.diabout.helpers.YearValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class StatsFragment : Fragment() {

    lateinit var userDBHandler : UserDBHelper
    lateinit var allRecords: List<RecordItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stats, container, false)

        val bundle = arguments
        val userID = bundle!!.getString("ID")

        userDBHandler = UserDBHelper(context)

        val text1 = view.findViewById<View>(R.id.barChartTitleTV) as TextView

        val daysInMonth = Calendar.getInstance().firstDayOfWeek
        text1.text = daysInMonth.toString()

        var recordTypeSelected = 1
        var timeSelected = 1

        val recordTypeLayout = view.findViewById<View>(R.id.recordTypeTabs) as TabLayout
        recordTypeLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                recordTypeSelected = tab.position + 1

                if (timeSelected == 0){
                    setGraphDataWeekly(view , userID!!, recordTypeSelected)
                } else if (timeSelected == 1){
                    setGraphDataMonthly(view, userID!!, recordTypeSelected)
                } else {
                    setGraphDataYearly(view, userID!!, recordTypeSelected)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        val tabLayout = view.findViewById<View>(R.id.dateTabs) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                timeSelected = tab.position
                if (timeSelected == 0){
                    setGraphDataWeekly(view , userID!!, recordTypeSelected)
                } else if (timeSelected == 1){
                    setGraphDataMonthly(view, userID!!, recordTypeSelected)
                } else {
                    setGraphDataYearly(view, userID!!, recordTypeSelected)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }

    fun setGraphDataWeekly(view : View, userID : String, recordTypeSelected : Int) {
        val entries = ArrayList<Entry>()

        entries.add(Entry(1f, 1f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 3f))
        entries.add(Entry(4f, 4f))
        entries.add(Entry(5f, 5f))
        entries.add(Entry(6f, 6f))
        entries.add(Entry(7f, 7f))

        val v1 = LineDataSet(entries, "My Type")

        v1.setDrawValues(false)
        v1.setDrawFilled(false)
        v1.lineWidth = 3f
        v1.fillColor = R.color.main_blue

        createLineChart(view, entries, 0)
    }

    fun setGraphDataMonthly(view : View, userID : String, recordTypeSelected : Int) {

        val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentYear = 2024
        val currentMonth = 4

        val entries = ArrayList<Entry>()

        allRecords = userDBHandler.findAllUserRecords(userID.toInt())

        val perDayData = Array(daysInMonth) {Array<Int>(2){0} }

        for (i in allRecords){
            println(i.recordtype.toString() + " " + recordTypeSelected.toString())
            if (i.recordtype == recordTypeSelected) {
                val date = i.time.split(" ")
                val dateSplit = date[0].split("-")
                if (dateSplit[0].toInt() == currentYear){
                    if (dateSplit[1].toInt() == currentMonth){
                        val day = dateSplit[2].toInt()

                        if (perDayData[day][1] == 0){
                            perDayData[day][0] = i.value
                            perDayData[day][1] = 1
                        } else {
                            perDayData[day][0] += i.value
                            perDayData[day][1] += 1
                        }
                    }
                }
            }
        }

        var count = 0
        for (j in perDayData){
            if (j[0] != 0) {
                val average = j[0] / j[1]
                entries.add(Entry(count.toFloat(),average.toFloat()))
            } else {
                entries.add(Entry(count.toFloat(),0f))
            }
            count++
        }

        createLineChart(view, entries, 1)

    }

    fun setGraphDataYearly(view : View, userID : String, recordTypeSelected : Int){

        val currentYear = 2024

        val entries = ArrayList<Entry>()

        allRecords = userDBHandler.findAllUserRecords(userID.toInt())

        val perMonthData = Array(12) {Array<Int>(2){0} }

        for (i in allRecords){
            println(i.recordtype.toString() + " " + recordTypeSelected.toString())
            if (i.recordtype == recordTypeSelected) {
                val date = i.time.split(" ")
                val dateSplit = date[0].split("-")
                if (dateSplit[0].toInt() == currentYear){
                    val month = dateSplit[1].toInt()
                    if (perMonthData[month][1] == 0){
                        perMonthData[month][0] = i.value
                        perMonthData[month][1] = 1
                    } else {
                        perMonthData[month][0] += i.value
                        perMonthData[month][1] += 1
                    }
                }
            }
        }

        var count = 0
        for (j in perMonthData){
            if (j[0] != 0) {
                val average = j[0] / j[1]
                entries.add(Entry(count.toFloat(),average.toFloat()))
            } else {
                entries.add(Entry(count.toFloat(),0f))
            }
            count++
        }

        createLineChart(view, entries, 2)
    }

    fun createLineChart(view : View, entries : ArrayList<Entry>, timeSelected : Int){
        val lineChart = view.findViewById<View>(R.id.chart) as LineChart
        val v1 = LineDataSet(entries, "My Type")

//        v1.setDrawValues(false)
//        v1.setDrawFilled(false)
//        v1.lineWidth = 3f
//        v1.fillColor = R.color.main_blue

//        lineChart.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(v1)

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setScaleEnabled(false)

        if(timeSelected == 1){
            lineChart.xAxis.valueFormatter = MonthValueFormatter()
        }else if (timeSelected == 2){
            lineChart.xAxis.valueFormatter = YearValueFormatter()
            lineChart.xAxis.setLabelCount(12)
        }

        lineChart.xAxis.setDrawGridLines(false)

        lineChart.axisRight.isEnabled = false

//        lineChart.description.text = "Days"
//        lineChart.setNoDataText("none")

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    fun resetChart(view : View){
        val lineChart = view.findViewById<View>(R.id.chart) as LineChart
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}