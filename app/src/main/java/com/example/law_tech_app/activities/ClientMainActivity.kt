package com.example.law_tech_app.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.law_tech_app.R

import com.google.android.material.bottomnavigation.BottomNavigationView

class ClientMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client)
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
            this@ClientMainActivity,
            R.drawable.app_gradient_color_background))

        val navView : BottomNavigationView = findViewById(R.id.menu_main_client)
        val navController = findNavController(R.id.fragmentContainer_client_container)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.icon_menu_search,
            R.id.icon_menu_profile,
            R.id.icon_menu_notifications
        ))
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}