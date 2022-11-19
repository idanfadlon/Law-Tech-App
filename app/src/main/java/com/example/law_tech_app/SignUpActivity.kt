package com.example.law_tech_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : BaseActivity() {
    lateinit var tieFullName: TextInputEditText
    lateinit var tieEmail: TextInputEditText
    lateinit var tiePassword: TextInputEditText
    lateinit var tieConfirmPassword: TextInputEditText
    lateinit var tieLicenseNumber: TextInputEditText
    lateinit var tiePhoneNumber: TextInputEditText
    lateinit var tieSummary: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}