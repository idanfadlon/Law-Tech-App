package com.example.law_tech_app.activities


import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.fragments.LawyerNotificationsFragment
import com.example.law_tech_app.fragments.LawyerProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class LawyerMainActivity : BaseActivity() {
//    private val calendarFragment = LawyerCalendarFragment()
//    private val notificationsFragment = LawyerNotificationsFragment()
//    private val profileFragment = LawyerProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lawyer)
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(
            this@LawyerMainActivity,
            R.drawable.app_gradient_color_background))

        val navView :BottomNavigationView = findViewById(R.id.menu_main_lawyer)
        val navController = findNavController(R.id.fragmentContainer_lawyer_container)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.icon_menu_calendar,
            R.id.icon_menu_profile,
            R.id.icon_menu_notifications
        ))
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)

//        replaceFragment(calendarFragment)
//        val menubar = findViewById<BottomNavigationView>(R.id.menu_main_lawyer)
//        menubar.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.icon_menu_calendar -> replaceFragment(calendarFragment)
//                R.id.icon_menu_notifications -> replaceFragment(notificationsFragment)
//                R.id.icon_menu_profile -> replaceFragment(profileFragment)
//            }
//            true
//        }

    }

//    private fun replaceFragment(fragment: Fragment){
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainer_lawyer_container,fragment)
//        transaction.commit()
//    }

}

