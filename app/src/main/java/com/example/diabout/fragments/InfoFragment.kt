package com.example.diabout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.helpers.Info
import com.example.diabout.helpers.InfoAdapter
import com.example.diabout.helpers.InfoList


class InfoFragment : Fragment(R.layout.fragment_info) {

    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_info, container, false)

        populateInfoList()
        //adds the info to the recycler view
        recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(container!!.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = InfoAdapter(InfoList)
        return view
    }

    //info to add to the view
    private fun populateInfoList() {
        val infoCard1 = Info("What Is Type 1 Diabetes?",
            "The first step in self managing you diabetes is understanding it. " +
                    "The more you learn about your diabetes the easier it will be to self manage",
            "https://www.cdc.gov/diabetes/basics/what-is-type-1-diabetes.html")
        InfoList.add(infoCard1)

        val infoCard2 = Info("Ten Tips for Self-Management",
            "Using this app is a good way of keeping on top of the self management of your diabetes" +
                    ", however there are other self management steps that you can do at home to improve " +
                    "your life with diabetes. ",
            "https://www.kidney.org/atoz/content/diabetes-ten-tips-self-management")
        InfoList.add(infoCard2)

        val infoCard3 = Info("Why is Dietary Intake important for Diabetes?",
            "You may find it tedious to keep track of the foods and calories you are consuming, " +
                    "however it can be very beneficial",
            "https://www.frontiersin.org/articles/10.3389/fnut.2021.782670/full")
        InfoList.add(infoCard3)

        val infoCard4 = Info("Diabetes and Exercise",
            "Exercise is an important part of anyone's lifestyle, but it's particularly important " +
                    "for glycaemic control in people with diabetes",
            "https://bjsm.bmj.com/content/33/3/161")
        InfoList.add(infoCard4)

        val infoCard5 = Info("Why using Wearable devices can aid the management of diabetes",
            "Although purchasing a wearable device won't cure your diabetes, it has been " +
                    "proven to promote physical activity within patients",
            "https://www.emjreviews.com/diabetes/article/efficacy-of-wearable-devices-to-measure-and-promote-physical-activity-in-the-management-of-diabetes/")
        InfoList.add(infoCard5)

        val infoCard6 = Info("Calculating Bolus Injections",
            "Calculating the correct amount of insulin to take is critical for anyone with " +
                    "type 1 diabetes. You can use our handy calculator, but it's also useful to know " +
                    "the calculations yourself",
            "https://www.nationwidechildrens.org/family-resources-education/health-wellness-and-safety-resources/resources-for-parents-and-kids/managing-your-diabetes/chapter-seven-calculating-bolus-injections")
        InfoList.add(infoCard6)
    }



}