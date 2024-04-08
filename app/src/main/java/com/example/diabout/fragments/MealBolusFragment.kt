package com.example.diabout.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.diabout.R
import com.example.diabout.activities.UserDetails

class MealBolusFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_meal_bolus, container, false)

        val gCarbsVal = view.findViewById<View>(R.id.gCarbs) as EditText
        val carbRatioVal = view.findViewById<View>(R.id.carbRatio) as EditText

        val calcButton = view.findViewById<View>(R.id.calcButton) as Button
        calcButton.setOnClickListener {

            val gCarbs = gCarbsVal.text.toString().toInt()
            val carbRatio = carbRatioVal.text.toString().toInt()

            val calculationResult = view.findViewById<View>(R.id.calculationResult) as TextView

            val carbBolus = gCarbs / carbRatio
            calculationResult.text = "Carbohydrate Bolus : $carbBolus units"

        }

        return view
    }
}