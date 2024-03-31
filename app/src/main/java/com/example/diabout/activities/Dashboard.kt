package com.example.diabout.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.diabout.R
import com.example.diabout.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val homeFragment = HomeFragment()
        val statsFragment = StatsFragment()
        val calcFragment = CalcFragment()
        val infoFragment = InfoFragment()
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)!!

        val intent = intent
        val userID = intent.getStringExtra("ID")

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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()

    }
}