package com.example.law_tech_app.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioGroup
import com.example.law_tech_app.BaseActivity
import com.example.law_tech_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SignUpActivity : BaseActivity() {
    lateinit var tieFullName: TextInputEditText
    lateinit var tieEmail: TextInputEditText
    lateinit var tiePassword: TextInputEditText
    lateinit var tieConfirmPassword: TextInputEditText
    lateinit var tiePhoneNumber: TextInputEditText
    lateinit var tieLicenseNumber: TextInputEditText
    lateinit var tieSummary: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
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
        radioGroup.setOnCheckedChangeListener{_,checkedId->
            onRadioClick(checkedId)
        }
        tieLicenseNumber=findViewById(R.id.tie_licenseNumber)
        tieSummary=findViewById(R.id.tie_summary)
    }

    private fun onRadioClick(checkedId: Int) {
        if(checkedId== R.id.rb_lawyer){
            val tilLicenseNumber=findViewById<TextInputLayout>(R.id.til_licenseNumber)
            val tilSummary=findViewById<TextInputLayout>(R.id.til_summary)
            tilLicenseNumber.visibility=View.VISIBLE
            tieLicenseNumber.visibility=View.VISIBLE
            tilSummary.visibility=View.VISIBLE
            tieSummary.visibility=View.VISIBLE

        }
        else{
            val tilLicenseNumber=findViewById<TextInputLayout>(R.id.til_licenseNumber)
            val tilSummary=findViewById<TextInputLayout>(R.id.til_summary)
            tilLicenseNumber.visibility=View.GONE
            tieLicenseNumber.visibility=View.GONE
            tilSummary.visibility=View.GONE
            tieSummary.visibility=View.GONE
        }

    }
}