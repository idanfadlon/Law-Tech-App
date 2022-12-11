package com.example.law_tech_app.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast

import com.example.law_tech_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
//    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var btnSubmit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        btnSubmit = findViewById(R.id.btn_forgotpassword_submit)
        btnSubmit.setOnClickListener{
            val tie_password:TextInputEditText = findViewById(R.id.tie_forgotpassword_email)
            val email:String = tie_password.text.toString().trim { it<=' '}
            if(email.isEmpty())
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            else{
                showProgressDialog(resources.getString(R.string.loading))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            resources.getString(R.string.email_sent),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }

//        setupActionBar()
    }

//    private fun setupActionBar(){
//        toolbar = findViewById(R.id.toolbar_forgot_toolbar)
//        setSupportActionBar(toolbar)
//        val actionBar = supportActionBar
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
//        }
//        toolbar.setNavigationOnClickListener{onBackPressed()}
//
//
//    }


}
