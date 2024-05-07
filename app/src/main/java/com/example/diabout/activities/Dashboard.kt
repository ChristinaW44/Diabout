package com.example.diabout.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper
import com.example.diabout.fragments.CalcFragment
import com.example.diabout.fragments.HomeFragment
import com.example.diabout.fragments.InfoFragment
import com.example.diabout.fragments.StatsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userDBHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //gets all the fragments that can be displayed
        val homeFragment = HomeFragment()
        val statsFragment = StatsFragment()
        val calcFragment = CalcFragment()
        val infoFragment = InfoFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)!!
        userDBHandler = UserDBHelper(this)

        setFragment(homeFragment)

        //finds the selected navigation button and selects the corresponding fragment
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navHome-> {
                    setFragment(homeFragment)
                    true
                }
                R.id.navStats -> {
                    setFragment(statsFragment)
                    true
                }
                R.id.navCalc-> {
                    setFragment(calcFragment)
                    true
                }
                R.id.navInfo -> {
                    setFragment(infoFragment)
                    true
                }
                else -> {false}
            }
        }
    }

    //displays the corresponding fragment to the navigation button selected
    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()

    }
}