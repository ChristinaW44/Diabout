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

class CorrectionBolusFragment : Fragment(){
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_correction_bolus, container, false)

        val gReadingVal = view.findViewById<View>(R.id.gReading) as EditText
        val gTargetVal = view.findViewById<View>(R.id.gTarget) as EditText
        val correctFactorVal = view.findViewById<View>(R.id.correctFactor) as EditText

        val targetInfoButton = view.findViewById<View>(R.id.targetInfo) as ImageButton
        targetInfoButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setTitle("What is Glucose Target?")
                .setMessage("Is the highest number in the target range. Example: If the target range is 70 to 120, the correction target will be 120")
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }

        val correctInfoButton = view.findViewById<View>(R.id.correctInfo) as ImageButton
        correctInfoButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setTitle("What is Correction Factor?")
                .setMessage("How many points 1 unit of insulin will lower the blood glucose")
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }

        val calcButton = view.findViewById<View>(R.id.calcButton) as Button
        calcButton.setOnClickListener {

            val gReading = gReadingVal.text.toString()
            val gTarget = gTargetVal.text.toString()
            val correctFactor = correctFactorVal.text.toString()

            val amountCorrectText = view.findViewById<View>(R.id.amountCorrectText) as TextView

            if (gReading != ""){
                if (gTarget != "") {
                    if (correctFactor != ""){
                        val amountCorrect = gReading.toInt() - gTarget.toInt()
                        amountCorrectText.text = amountCorrect.toString()
                        val correctBolus = amountCorrect / correctFactor.toInt()
                        val calculationResult = view.findViewById<View>(R.id.calculationResult) as TextView
                        calculationResult.text = "Glucose Correction Bolus : $correctBolus units"
                    } else
                        Toast.makeText(context, "Please enter a value for glucose correction factor", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(context, "Please enter a value for glucose target", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context, "Please enter a value for glucose reading", Toast.LENGTH_SHORT).show()
        }


        return view
    }
}