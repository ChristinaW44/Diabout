package com.example.diabout.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.activities.UserDetails
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stats, container, false)

        var currentPos = 0
        val text1 = view.findViewById<View>(R.id.barChartTitleTV) as TextView
        text1.text = currentPos.toString()


        val tabLayout = view.findViewById<View>(R.id.dateTabs) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                currentPos = tab.position
                text1.text = currentPos.toString()
                if (currentPos == 0){
                    setGraphDataWeekly(view)
                } else if (currentPos == 1){
                    setGraphDataMonthly(view)
                } else {
                    setGraphDataYearly(view)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }

    fun setGraphDataWeekly(view : View) {
        val lineChart = view.findViewById<View>(R.id.chart) as LineChart
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

        lineChart.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(v1)

        lineChart.axisRight.isEnabled = false

        lineChart.description.text = "Days"
        lineChart.setNoDataText("none")

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    fun setGraphDataMonthly(view : View) {
        val lineChart = view.findViewById<View>(R.id.chart) as LineChart
        val entries = ArrayList<Entry>()

        entries.add(Entry(1f, 1f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 3f))
        entries.add(Entry(4f, 4f))
        entries.add(Entry(5f, 5f))
        entries.add(Entry(6f, 6f))
        entries.add(Entry(7f, 7f))
        entries.add(Entry(8f, 8f))
        entries.add(Entry(9f, 9f))
        entries.add(Entry(10f, 10f))
        entries.add(Entry(11f, 11f))
        entries.add(Entry(12f, 12f))
        entries.add(Entry(13f, 13f))
        entries.add(Entry(14f, 4f))
        entries.add(Entry(15f, 5f))
        entries.add(Entry(16f, 6f))
        entries.add(Entry(17f, 7f))
        entries.add(Entry(18f, 8f))
        entries.add(Entry(19f, 9f))



        val v1 = LineDataSet(entries, "My Type")

        v1.setDrawValues(false)
        v1.setDrawFilled(false)
        v1.lineWidth = 3f
        v1.fillColor = R.color.main_blue

        lineChart.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(v1)

        lineChart.axisRight.isEnabled = false

        lineChart.description.text = "Days"
        lineChart.setNoDataText("none")

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    fun setGraphDataYearly(view : View){

        val currentYear = 2024
        val recordType = 1

        val lineChart = view.findViewById<View>(R.id.chart) as LineChart
        val entries = ArrayList<Entry>()

        entries.add(Entry(1f,1f))
        entries.add(Entry(2f,2f))
        entries.add(Entry(3f,3f))
        entries.add(Entry(4f,4f))
        entries.add(Entry(5f,5f))
        entries.add(Entry(6f,6f))
        entries.add(Entry(7f,7f))
        entries.add(Entry(8f,1f))
        entries.add(Entry(9f,2f))
        entries.add(Entry(10f,3f))
        entries.add(Entry(11f,4f))
        entries.add(Entry(12f,5f))

        val v1 = LineDataSet(entries, "My Type")

        v1.setDrawValues(false)
        v1.setDrawFilled(false)
        v1.lineWidth = 3f
        v1.fillColor = R.color.main_blue

        lineChart.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(v1)

        lineChart.axisRight.isEnabled = false

        lineChart.description.text = "Days"
        lineChart.setNoDataText("none")

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}