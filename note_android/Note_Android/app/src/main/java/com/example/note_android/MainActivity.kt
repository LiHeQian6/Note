package com.example.note_android

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_follow, R.id.navigation_persion
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.findViewById<View>(R.id.navigation_home).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_follow).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_persion).setOnLongClickListener { true }
        navView.setOnNavigationItemReselectedListener { true }
        navView.setupWithNavController(navController)
    }
}