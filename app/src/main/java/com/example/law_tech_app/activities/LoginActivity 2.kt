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
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*


class LoginActivity: BaseActivity() {
    lateinit var tie_email: TextInputEditText
    lateinit var tie_password: TextInputEditText
    lateinit var login_btn:Button
    lateinit var forgotPasswordTv:TextView
    lateinit var tvRegister:TextView
    lateinit var radioGroup:RadioGroup

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
        radioGroup = findViewById(R.id.radioGroup)
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
            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        }
    }

private fun onRadioClick(id:Int){
    if(id== R.id.rb_client)
        tie_email.hint = "Email"
    else {
        tie_email.hint = "License Number"
    }
    }
//    fun checkCurrentUserType(uid:String):Boolean{
//        FirestoreClass().mFireStore.collection(Constants.LAWYERS).document(uid)
//    }

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
    fun userLoggedInSuccess(currentUser:User?)
    {

        if (currentUser != null)
        {

            when(currentUser.toString())
            {
                Constants.LAWYERS -> {
                    hideProgressDialog()
                    val intent = Intent(this@LoginActivity, LawyerMainActivity::class.java)
                    intent.putExtra(Constants.CURRENT_EXTRA_USER_DETAILS, currentUser as Lawyer)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    hideProgressDialog()
                    val intent = Intent(this@LoginActivity, ClientMainActivity::class.java)
                    intent.putExtra(Constants.CURRENT_EXTRA_USER_DETAILS, currentUser as Client)
                    startActivity(intent)
                    finish()
                }
            }

        }
        else
        {
            hideProgressDialog()
            showErrorSnackBar("The chosen type is not corresponding the registered user type !", true)
            FirebaseAuth.getInstance().signOut()
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
                        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                        when (this.radioGroup.checkedRadioButtonId) {
                            R.id.rb_lawyer -> {
                                FirestoreClass().getCurrentUserDetails(this@LoginActivity,Constants.LAWYERS,currentUserID)
                            }
                            R.id.rb_client -> {
                                FirestoreClass().getCurrentUserDetails(this@LoginActivity,Constants.CLIENTS,currentUserID)
                            }
                            else -> {
                                showErrorSnackBar("Error while singing in",true)
                            }
                        }

                    } else {
                        // Hide the progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

}







