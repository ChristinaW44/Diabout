package com.example.diabout.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.activities.RecordActivity
import com.example.diabout.activities.RecordFood
import com.example.diabout.activities.RecordGlucose
import com.example.diabout.activities.TargetGlucose
import com.example.diabout.activities.UserDetails
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.RecyclerAdapter
import java.text.SimpleDateFormat
import java.util.Calendar

class HomeFragment : Fragment() {

    lateinit var welcomeText : TextView
    lateinit var userDBHandler : UserDBHelper
    lateinit var recyclerView: RecyclerView
    lateinit var allRecords: List<RecordItem>
    lateinit var recordList: List<RecordItem>
    lateinit var dateList: MutableList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        welcomeText = view.findViewById<View>(R.id.welcomeText) as TextView

        val bundle = arguments
        val name = bundle!!.getString("name")
        val userID = bundle.getString("ID")

        welcomeText.text = "Hello $name"

        val userProfileButton = view.findViewById<View>(R.id.userProfileButton) as ImageButton
        userProfileButton.setOnClickListener {
            val intent = Intent(context, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }

        val glucoseRecordButton = view.findViewById<View>(R.id.glucoseRecordButton) as ImageButton
        glucoseRecordButton.setOnClickListener {
            val intent = Intent(context, RecordGlucose::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }

        val activityRecordButton = view.findViewById<View>(R.id.activityRecordButton) as ImageButton
        activityRecordButton.setOnClickListener {
            val intent = Intent(context, RecordActivity::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }

        val foodRecordButton = view.findViewById<View>(R.id.foodRecordButton) as ImageButton
        foodRecordButton.setOnClickListener {
            val intent = Intent(context, RecordFood::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }

        val targetGlucoseButton = view.findViewById<View>(R.id.targetGlucose) as ImageButton
        targetGlucoseButton.setOnClickListener {
            val intent = Intent(context, TargetGlucose::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }

        userDBHandler = UserDBHelper(context)

        allRecords = userDBHandler.findAllUserRecords(userID!!.toInt())

        recordList = allRecords.sortedByDescending { it.time }

        dateList = mutableListOf<String>()

        for (i in recordList){
            val date = i.time.split(" ")
            val formatedDate = formatDate(date[0])
            if (dateList.contains(formatedDate)){
                dateList.add("")
            } else {
                dateList.add(formatedDate)
            }
        }

        recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(container!!.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.adapter = RecyclerAdapter(recordList, dateList)

        setTodaysRecordsText(allRecords, view)

        return view
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val current = formatter.format(time)
        return current
    }

    private fun setTodaysRecordsText(allRecords: List<RecordItem>, view : View) {
        
        val todaysDate = getTodaysDate()

        var totalGlucose = 0
        var glucoseCount = 0
        var totalSteps = 0
        var totalCarbs = 0
        
        for(i in allRecords){
            val date = i.time.split(" ")[0]
            if (date == todaysDate){
                if (i.recordtype == 1){
                    totalGlucose += i.value
                    glucoseCount ++
                } else if (i.recordtype == 2)
                    totalSteps += i.value
                else
                    totalCarbs +=i.value
            }
            
        }
        var averageGlucose = 0
        val glucoseText = view.findViewById<View>(R.id.glucoseText) as TextView
        if (glucoseCount>0){
            if (totalGlucose>0){
                averageGlucose = totalGlucose /glucoseCount
                println(averageGlucose)
            }
        }
        glucoseText.text = averageGlucose.toString() + " mg/dL"

        val stepsText = view.findViewById<View>(R.id.stepsText) as TextView
        stepsText.text = totalSteps.toString() + " steps"

        val carbsText = view.findViewById<View>(R.id.carbsText) as TextView
        carbsText.text = totalCarbs.toString() + " g"
    }

    fun formatDate(date : String) : String{
        val splitDate = date.split("-")
        var month = ""
        if(splitDate[1] == "01"){
            month = "January"
        } else if (splitDate[1] == "02"){
            month = "February"
        }else if (splitDate[1] == "03"){
            month = "March"
        }else if (splitDate[1] == "04"){
            month = "April"
        }else if (splitDate[1] == "05"){
            month = "May"
        }else if (splitDate[1] == "06"){
            month = "June"
        }else if (splitDate[1] == "07"){
            month = "July"
        }else if (splitDate[1] == "08"){
            month = "August"
        }else if (splitDate[1] == "09"){
            month = "September"
        }else if (splitDate[1] == "10"){
            month = "October"
        }else if (splitDate[1] == "11"){
            month = "November"
        }else {
            month = "December"
        }

        val newDate = splitDate[2] + " " + month + " " + splitDate[0]
        return newDate
    }
}