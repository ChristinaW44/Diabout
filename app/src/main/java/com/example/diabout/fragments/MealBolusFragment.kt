package com.example.diabout.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.diabout.R


class MealBolusFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_meal_bolus, container, false)

        val gCarbsVal = view.findViewById<View>(R.id.gCarbs) as EditText
        val carbRatioVal = view.findViewById<View>(R.id.carbRatio) as EditText

        val ratioInfoButton = view.findViewById<View>(R.id.ratioInfo) as ImageButton
        ratioInfoButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setTitle("What is Carbohydrate Ratio?")
                .setMessage("How many grams of carbohydrate will be covered by 1 unit of insulin")
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }

        val calcButton = view.findViewById<View>(R.id.calcButton) as Button
        calcButton.setOnClickListener {

            val gCarbs = gCarbsVal.text.toString()
            val carbRatio = carbRatioVal.text.toString()

            val calculationResult = view.findViewById<View>(R.id.calculationResult) as TextView

            if (gCarbs != ""){
                if (carbRatio != ""){
                    val carbBolus = gCarbs.toInt() / carbRatio.toInt()
                    calculationResult.text = "Carbohydrate Bolus : $carbBolus units"
                }else
                    Toast.makeText(context, "Please enter a value for carbohydrate ratio", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context, "Please enter a value for grams of carbohydrates", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}