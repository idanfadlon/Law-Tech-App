package com.example.law_tech_app.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())


        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN

            )
        }
        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
//                if(mAuth.currentUser !=null) {
//                    startActivity(Intent(this@SplashActivity, LawyerMainActivity::class.java))
//                    finish()
//                }else{
//                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
//                    finish()
//                }
//                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                startActivity(Intent(this@SplashActivity, SignUpActivity::class.java))
                 finish()

            },
            2000
        )
    }
}