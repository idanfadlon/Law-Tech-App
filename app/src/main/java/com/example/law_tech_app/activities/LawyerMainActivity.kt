package com.example.law_tech_app.activities


import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toIcon
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.fragments.LawyerNotificationsFragment
import com.example.law_tech_app.fragments.LawyerProfileFragment
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.io.IOException
/*
need to add permissions dialog when user in logged in for the first time
*/
class LawyerMainActivity : BaseActivity() {
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

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK)
        {
            if (requestCode == Constants.IMAGE_UPLOAD_CODE) {
                if (data != null) {
                    try {
                        showProgressDialog(resources.getString(R.string.loading))
                        val selectedImageFileURI = data.data!!
                        val sref: StorageReference =
                            FirebaseStorage.getInstance().reference.child("Image " + FirestoreClass().getCurrentUserID() + ".jpg")
                        sref.putFile(selectedImageFileURI).addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                                val currentUserHashMap = HashMap<String, Any>()
                                currentUserHashMap[Constants.IMAGE_URL] = url.toString()
                                FirestoreClass().updateCurrentUserDetails(
                                    currentUserHashMap,
                                    Constants.LAWYERS,
                                    null
                                )


                            }
                        }
                        hideProgressDialog()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }else if(requestCode == Constants.IMAGE_REQUEST_CODE){
                if (data !=null)
                {
                    Log.e("matan", data.data.toString())
                }
            }
        }
        else{
            showErrorSnackBar("Failed on trying taking a picture",true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.hasExtra(Constants.IS_EVENT_ADDED)){
            showErrorSnackBar("Event was successfully added !",false)
            intent.removeExtra(Constants.IS_EVENT_ADDED)
        }
    }

}

