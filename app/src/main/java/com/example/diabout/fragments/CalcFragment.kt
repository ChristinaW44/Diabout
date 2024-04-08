package com.example.diabout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.diabout.R
import com.google.android.material.tabs.TabLayout

class CalcFragment : Fragment() {

    lateinit var tabLayout : TabLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_calc, container, false)


        val mealBolusFragment = MealBolusFragment()
        val correctionBolusFragment = CorrectionBolusFragment()
        setFragment(mealBolusFragment,childFragmentManager)


        tabLayout = view.findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 0){
                    setFragment(mealBolusFragment,childFragmentManager)
                } else {
                    setFragment(correctionBolusFragment,childFragmentManager)
                }
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })
        return view
    }
}

private fun setFragment(fragment: Fragment, fragmentManager: FragmentManager) {
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.fragment, fragment)
    transaction.commit()

}