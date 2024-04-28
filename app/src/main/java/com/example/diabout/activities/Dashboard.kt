package com.example.diabout.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper
import com.example.diabout.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userDBHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val homeFragment = HomeFragment()
        val statsFragment = StatsFragment()
        val calcFragment = CalcFragment()
        val infoFragment = InfoFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)!!
        userDBHandler = UserDBHelper(this)
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("ID", "0")
        val name = sharedPreferences.getString("name", "")
//        val intent = intent
//        val userID = intent.getStringExtra("ID")
//        val name = userDBHandler.getNameFromID(userID!!)

        setFragment(homeFragment)

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



    private fun setFragment(fragment: Fragment) {
//        val bundle = Bundle()
//        editSharedPrefs.putString("ID", userID)

//        bundle.putString("name", name)
//        bundle.putString("ID", userID)
//        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()

    }
}