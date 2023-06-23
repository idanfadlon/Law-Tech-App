package com.example.law_tech_app.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.utils.Constants

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            if (requestCode == Constants.IMAGE_UPLOAD_CODE) {
                if (data != null) {
                    try {
                        val selectedImageFileURI = data.data!!
                        val sref: StorageReference =
                            FirebaseStorage.getInstance().reference.child("Image " + FirestoreClass().getCurrentUserID() + ".jpg")
                        sref.putFile(selectedImageFileURI).addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                                val currentUserHashMap = HashMap<String, Any>()
                                currentUserHashMap[Constants.IMAGE_URL] = url.toString()
                                FirestoreClass().updateCurrentUserDetails(
                                    currentUserHashMap,
                                    Constants.CLIENTS,
                                    null
                                )

                            }
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }else if(requestCode == Constants.IMAGE_REQUEST_CODE){
                if (data !=null && data.extras !=null)
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
    }

}

