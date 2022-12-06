package com.example.law_tech_app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.law_tech_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity: BaseActivity() {
    lateinit var tie_email: TextInputEditText
    lateinit var tie_password: TextInputEditText
    lateinit var login_btn:Button
    lateinit var forgotPasswordTv:TextView
    lateinit var tvRegister:TextView



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val radioGroup:RadioGroup = findViewById(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener{_, checkedId ->
           onRadioClick(checkedId)
        }
        tie_email = findViewById(R.id.tie_forgotpassword_email)
        tie_email.hint=("Email")
        tie_password = findViewById(R.id.tie_password)
        tie_password.hint=("Password")
        login_btn = findViewById(R.id.btn_login)
        login_btn.setOnClickListener {
            logInRegisteredUser()
        }
        forgotPasswordTv = findViewById(R.id.forgot_password_tv)
        forgotPasswordTv.setOnClickListener{
            //TODO: add navigate to forgot password screen
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        tvRegister = findViewById(R.id.tv_login_register)
        tvRegister.setOnClickListener{
            //TODO: add navigate to register screen
            onBackPressed()
        }

    }

private fun onRadioClick(id:Int){
    if(id== R.id.rb_client)
        tie_email.hint = "Email"
    else {
        tie_email.hint = "License Number"

    }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(tie_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(tie_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.loading))

            // Get the text from editText and trim the space
            val email = tie_email.text.toString().trim { it <= ' ' }
            val password = tie_password.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideProgressDialog()
                        val intent = Intent(this@LoginActivity, LawyerMainActivity::class.java)
                        startActivity(intent)
                        finish()

//                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        // Hide the progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

}







