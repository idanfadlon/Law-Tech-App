package com.example.law_tech_app.activities


import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.fragments.LawyerNotificationsFragment
import com.example.law_tech_app.fragments.LawyerProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest

class LawyerMainActivity : BaseActivity() {
    private val calendarFragment = LawyerCalendarFragment()
    private val notificationsFragment = LawyerNotificationsFragment()
    private val profileFragment = LawyerProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lawyer)
        replaceFragment(calendarFragment)
        val menubar = findViewById<BottomNavigationView>(R.id.menubar_main_lawyer)
        menubar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.icon_menu_calendar -> replaceFragment(calendarFragment)
                R.id.icon_menu_notifications -> replaceFragment(notificationsFragment)
                R.id.icon_menu_profile -> replaceFragment(profileFragment)
            }
            true
        }



    }

    companion object{
        const val CAMERA_PERMISSION_CODE =1
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_lawyer_container,fragment)
        transaction.commit()
    }

}