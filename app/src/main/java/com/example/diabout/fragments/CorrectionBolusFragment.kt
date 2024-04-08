package com.example.diabout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.diabout.R

class CorrectionBolusFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_correction_bolus, container, false)

        val gReadingVal = view.findViewById<View>(R.id.gReading) as EditText
        val gTargetVal = view.findViewById<View>(R.id.gTarget) as EditText
        val correctFactorVal = view.findViewById<View>(R.id.correctFactor) as EditText

        val calcButton = view.findViewById<View>(R.id.calcButton) as Button
        calcButton.setOnClickListener {

            val gReading = gReadingVal.text.toString().toInt()
            val gTarget = gTargetVal.text.toString().toInt()

            val amountCorrectText = view.findViewById<View>(R.id.amountCorrectText) as TextView
            val amountCorrect = gReading - gTarget
            amountCorrectText.text = amountCorrect.toString()

            val correctFactor = correctFactorVal.text.toString().toInt()

            val correctBolus = amountCorrect / correctFactor
            val calculationResult = view.findViewById<View>(R.id.calculationResult) as TextView
            calculationResult.text = "Glucose Correction Bolus : $correctBolus units"

        }

        return view
    }
}