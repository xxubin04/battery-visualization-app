package com.xxubin04.batteryvisualizationapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.xxubin04.batteryvisualizationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

//        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_graph, R.id.navigation_data, R.id.navigation_tracking
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }
}