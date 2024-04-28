package com.example.diabout.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.MonthValueFormatter
import com.example.diabout.helpers.WeekValueFormatter
import com.example.diabout.helpers.YearValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.absoluteValue

class StatsFragment : Fragment() {

    lateinit var userDBHandler : UserDBHelper
    lateinit var allRecords: List<RecordItem>
    lateinit var titleText : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stats, container, false)

//        val bundle = arguments
//        val userID = bundle!!.getString("ID")
        val sharedPreferences = this.activity?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences?.getString("ID", "0")

        userDBHandler = UserDBHelper(context)

        titleText = view.findViewById<View>(R.id.title) as TextView
        titleText.text = "Average glucose levels (mg/dL)"

        var recordTypeSelected = 1
        var timeSelected = 0

        setGraphDataWeekly(view , userID!!, recordTypeSelected)

        val recordTypeLayout = view.findViewById<View>(R.id.recordTypeTabs) as TabLayout
        recordTypeLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                recordTypeSelected = tab.position + 1

                if (recordTypeSelected == 1)
                    titleText.text = "Average glucose levels (mg/dL)"
                else if (recordTypeSelected == 2)
                    titleText.text = "Total steps"
                else
                    titleText.text = "Total carbs consumed"

                if (timeSelected == 0){
                    setGraphDataWeekly(view , userID, recordTypeSelected)
                } else if (timeSelected == 1){
                    setGraphDataMonthly(view, userID, recordTypeSelected)
                } else {
                    setGraphDataYearly(view, userID, recordTypeSelected)
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
                    setGraphDataWeekly(view , userID, recordTypeSelected)
                } else if (timeSelected == 1){
                    setGraphDataMonthly(view, userID, recordTypeSelected)
                } else {
                    setGraphDataYearly(view, userID, recordTypeSelected)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }

    fun setGraphDataWeekly(view : View, userID : String, recordTypeSelected : Int) {
        val entries = ArrayList<BarEntry>()
        allRecords = userDBHandler.findAllUserRecords(userID.toInt())
        //gets the current day of the week as an integer, e.g. MONDAY = 1
        val current = LocalDate.now()
        val dayValue = current.dayOfWeek.value
        var count = 0
        //repeats until all days of current week have been added
        while (count != dayValue) {
            val newDate = getDateNeeded(current, (dayValue - count - 1).toLong())
            val currentYear = newDate.year
            val currentMonth = newDate.month
            val currentDay = newDate.dayOfMonth
            var total = 0
            var records = 0
            //adds all records for the day to the array
            for (i in allRecords){
                if (i.recordtype == recordTypeSelected) {
                    val recordDate = i.time.split(" ")
                    val recordDateSplit = recordDate[0].split("-")
                    if (recordDateSplit[0].toInt() == currentYear){
                        if (recordDateSplit[1].toInt() == currentMonth.value){
                            if (recordDateSplit[2].toInt() == currentDay){
                                total += i.value
                                records ++
                            }
                        }
                    }
                }
            }
            //if the record type is glucose finds the average for each day
            if (total != 0){
                if (recordTypeSelected == 1){
                    val average = total / records
                    entries.add(BarEntry((count).toFloat(),average.toFloat()))
                } else
                    entries.add(BarEntry((count).toFloat(),total.toFloat()))
            } else
                entries.add(BarEntry(count.toFloat(),0F))
            count ++
        }

        var restOfData = dayValue
        while (restOfData < 7){
            entries.add(BarEntry(restOfData.toFloat(),0F))
            restOfData ++
        }
        createLineChart(view, entries, 0)
    }

    private fun getDateNeeded(currentDay: LocalDate, daysBefore: Long): LocalDate {
        return currentDay.minusDays(daysBefore)
    }

    fun setGraphDataMonthly(view : View, userID : String, recordTypeSelected : Int) {
        //gets values for the current date
        val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        val entries = ArrayList<BarEntry>()
        allRecords = userDBHandler.findAllUserRecords(userID.toInt())
        val perDayData = Array(daysInMonth) {Array<Int>(2){0} }
        // adds the record data to the correct position in the array
        for (i in allRecords){
            if (i.recordtype == recordTypeSelected) {
                val date = i.time.split(" ")
                val dateSplit = date[0].split("-")
                if (dateSplit[0].toInt() == currentYear){
                    if (dateSplit[1].toInt() == currentMonth){
                        val day = dateSplit[2].toInt()
                        if (perDayData[day-1][1] == 0){
                            perDayData[day-1][0] = i.value
                            perDayData[day-1][1] = 1
                        } else {
                            perDayData[day-1][0] += i.value
                            perDayData[day-1][1] += 1
                        }
                    }
                }
            }
        }
        //turns the data for each day into an entry for the bar chart
        var count = 0
        for (j in perDayData){
            if (j[0] != 0) {
                if (recordTypeSelected == 1) {
                    val average = j[0] / j[1]
                    entries.add(BarEntry(count.toFloat(), average.toFloat()))
                } else
                    entries.add(BarEntry(count.toFloat(), j[0].toFloat()))
            } else
                entries.add(BarEntry(count.toFloat(),0f))
            count++
        }
        createLineChart(view, entries, 1)
    }

    fun setGraphDataYearly(view : View, userID : String, recordTypeSelected : Int){

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val entries = ArrayList<BarEntry>()
        allRecords = userDBHandler.findAllUserRecords(userID.toInt())

        val perMonthData = Array(12) {Array<Int>(2){0} }

        for (i in allRecords){
            if (i.recordtype == recordTypeSelected) {
                val date = i.time.split(" ")
                val dateSplit = date[0].split("-")
                if (dateSplit[0].toInt() == currentYear){
                    val month = dateSplit[1].toInt()
                    if (perMonthData[month-1][1] == 0){
                        perMonthData[month-1][0] = i.value
                        perMonthData[month-1][1] = 1
                    } else {
                        perMonthData[month-1][0] += i.value
                        perMonthData[month-1][1] += 1
                    }
                }
            }
        }

        var count = 0
        for (j in perMonthData){
            if (j[0] != 0) {
                if (recordTypeSelected == 1) {
                    val average = j[0] / j[1]
                    entries.add(BarEntry(count.toFloat(), average.toFloat()))
                } else
                    entries.add(BarEntry(count.toFloat(), j[0].toFloat()))
            } else {
                entries.add(BarEntry(count.toFloat(),0f))
            }
            count++
        }

        createLineChart(view, entries, 2)
    }

    private fun createLineChart(view : View, entries : ArrayList<BarEntry>, timeSelected : Int){
        val barChart = view.findViewById<View>(R.id.chart) as BarChart
        val v1 = BarDataSet(entries, "My Type")
        v1.setDrawValues(false)
        v1.setColor(ContextCompat.getColor(context!!, R.color.main_blue))
        barChart.data = BarData(v1)

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.axisLeft.axisMinimum= 0f
        barChart.axisRight.axisMinimum = 0f
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.animateY(500);
        barChart.xAxis.setDrawAxisLine(false)
        barChart.xAxis.setDrawGridLines(false)


        if(timeSelected == 1){
            barChart.xAxis.valueFormatter = MonthValueFormatter()
        }else if (timeSelected == 2){
            barChart.xAxis.valueFormatter = YearValueFormatter()
            barChart.xAxis.setLabelCount(12)
        } else {
            barChart.xAxis.valueFormatter = WeekValueFormatter()
            barChart.xAxis.setLabelCount(7)
        }

        barChart.axisRight.isEnabled = false

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}