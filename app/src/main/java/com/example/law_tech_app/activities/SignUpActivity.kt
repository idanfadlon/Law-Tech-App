package com.example.law_tech_app.activities
import android.content.Intent
import android.text.TextUtils
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.models.Lawyer
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SignUpActivity : com.example.law_tech_app.activities.BaseActivity() {
    lateinit var tieFullName: TextInputEditText
    lateinit var tieEmail: TextInputEditText
    lateinit var tiePassword: TextInputEditText
    lateinit var tieConfirmPassword: TextInputEditText
    lateinit var tiePhoneNumber: TextInputEditText
    lateinit var tieLicenseNumber: TextInputEditText
    lateinit var tieSpecialization: TextInputEditText
    lateinit var tieSummary: TextInputEditText
    lateinit var btnSignUp: Button
    lateinit var mAuth:FirebaseAuth
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
        tieFullName=findViewById(R.id.tie_fullName)
        tieEmail=findViewById(R.id.tie_email)
        tiePassword=findViewById(R.id.tie_password)
        tieConfirmPassword=findViewById(R.id.tie_confirmPassword)
        tiePhoneNumber=findViewById(R.id.tie_phoneNumber)
        tieLicenseNumber=findViewById(R.id.tie_licenseNumber)
        tieSpecialization =findViewById(R.id.tie_specialization)
        tieSummary=findViewById(R.id.tie_summary)
        btnSignUp=findViewById(R.id.btn_signup)
        mAuth =FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener { this.signUpUser() }
        val tvAlready:TextView=findViewById(R.id.tv_already)
        tvAlready.setOnClickListener{
            val intent =Intent(this@SignUpActivity,com.example.law_tech_app.activities.LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onRadioClick(checkedId: Int) {
        if(checkedId== R.id.rb_lawyer){
            val tilLicenseNumber=findViewById<TextInputLayout>(R.id.til_licenseNumber)
            val tilSummary=findViewById<TextInputLayout>(R.id.til_summary)
            val tilSpecialization=findViewById<TextInputLayout>(R.id.til_specialization)
            tilLicenseNumber.visibility=View.VISIBLE
            tieLicenseNumber.visibility=View.VISIBLE
            tilSummary.visibility=View.VISIBLE
            tieSummary.visibility=View.VISIBLE
            tilSpecialization.visibility=View.VISIBLE
            tieSpecialization.visibility=View.VISIBLE

        }
        else{
            val tilLicenseNumber=findViewById<TextInputLayout>(R.id.til_licenseNumber)
            val tilSummary=findViewById<TextInputLayout>(R.id.til_summary)
            val tilSpecialization=findViewById<TextInputLayout>(R.id.til_specialization)
            tilLicenseNumber.visibility=View.GONE
            tieLicenseNumber.visibility=View.GONE
            tilSummary.visibility=View.GONE
            tieSummary.visibility=View.GONE
            tilSpecialization.visibility=View.GONE
            tieSpecialization.visibility=View.GONE

        }

    }
    fun lawyerSignUpSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Signup as lawyer has been successfully completed",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@SignUpActivity, LawyerMainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun signUpUser(){
        val radioChecked=findViewById<RadioButton>(R.id.rb_lawyer)
        if(radioChecked.isChecked) {
            if (validateLawyerSignUpDetails()) {
                showProgressDialog(resources.getString(R.string.loading))
                createUserFirebase()
            }
        }
        else {
                if (validateClientSignUpDetails()) {
                    showProgressDialog(resources.getString(R.string.loading))
                    createUserFirebase()
                }
        }

    }
    private fun validateClientSignUpDetails():Boolean {

            return when {
                TextUtils.isEmpty(tieFullName.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar("Please enter full name", true)
                    false
                }
                tieFullName.text.toString().length<2->{
                    showErrorSnackBar("name must include at last 2 characters",true)
                    false
                }
                TextUtils.isEmpty(tieEmail.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar("Please enter full name", true)
                    false
                }
                TextUtils.isEmpty(tiePassword.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar("Please enter password", true)
                    false
                }
                TextUtils.isEmpty(tieConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar("Please re-enter password", true)
                    false
                }
                TextUtils.isEmpty(tiePhoneNumber.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar("Please enter phone number", true)
                    false
                }
                    tiePassword.text.toString().length<6 ->{
                    showErrorSnackBar("password must include at last 6 characters",true)
                    false
                }
                tiePassword.text.toString()!=tieConfirmPassword.text.toString() ->{
                    showErrorSnackBar("passwords are not matched",true)
                    false
                }
                //TODO:fix this also in lawyer validation
                !(tiePassword.text.toString().contains("[A-Za-z0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) ->{
                    Log.d("passnv","pass not valid")
                    showErrorSnackBar("password must include characters numbers and symbols",true)
                    false
                }

                else -> {
                    Log.d("passnv","im on else")
                    true
                }
            }

        }
    private fun validateLawyerSignUpDetails():Boolean{
        return when {
            TextUtils.isEmpty(tieFullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter full name", true)
                false
            }
            tieFullName.text.toString().length<2->{
                Log.d("fName","name not valid")
                showErrorSnackBar("name must include at last 2 characters",true)
                false
            }

            TextUtils.isEmpty(tieEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter email", true)
                false
            }
            TextUtils.isEmpty(tiePassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter password", true)
                false
            }
            TextUtils.isEmpty(tieConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please re-enter password", true)
                false
            }
            TextUtils.isEmpty(tiePhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter phone number", true)
                false
            }

            TextUtils.isEmpty(tieLicenseNumber.text.toString().trim { it<=' ' })->{
                showErrorSnackBar("Please enter license number",true)
                false
            }

            TextUtils.isEmpty(tieSpecialization.text.toString().trim { it<=' ' })->{
                showErrorSnackBar("Please enter specialization",true)
                false
            }

            TextUtils.isEmpty(tieSummary.text.toString().trim(){ it<=' '})->{
                showErrorSnackBar("Please tell a little about yourself",true)
                false
            }
            tiePassword.text.toString().length<6 ->{
                showErrorSnackBar("password must include at last 6 characters",true)
                false
            }
            tiePassword.text.toString()!=tieConfirmPassword.text.toString() ->{
                showErrorSnackBar("passwords are not matched",true)
                false
            }
            !(tiePassword.text.toString().contains("[A-Za-z0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) ->{
                Log.d("pass","pass not valid")
                showErrorSnackBar("password must include characters numbers and symbols",true)
                false
            }
            else -> {true}
        }

    }
    private fun createUserFirebase(){
        val email: String = tieEmail.text.toString().trim { it <= ' ' }
        val password: String = tiePassword.text.toString().trim { it <= ' ' }
        Log.d("register123456789","enter func")
        val imLawyer:Boolean = tieLicenseNumber.isVisible


        // Create an instance and create a register a user with email and password.
        Log.d("firebase","im in line 198")
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task: Task<AuthResult> ->
                    Log.d("task1","im in line 200")

            // If the registration is successfully done
                    if (task.isSuccessful) {
                        // Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        if (imLawyer) {
                            val lawyer = Lawyer(
                                firebaseUser.uid,
                                tieFullName.text.toString().trim{it <= ' '},
                                tieEmail.text.toString().trim {it <= ' '},
                                tieLicenseNumber.text.toString().trim {it <= ' '},
                                tieSpecialization.text.toString().trim {it <= ' '},
                                tiePhoneNumber.text.toString().trim {it <= ' '},
                                tieSummary.text.toString().trim {it <= ' '}
                            )
                            FirestoreClass().registerLawyer(this@SignUpActivity,lawyer)

                        }
                        else{

                            Log.d("sClient","signup from client option")
                            showErrorSnackBar("signup as client has been successfully completed", false)
                        }



                    } else {
                        hideProgressDialog()
                        // If the registering is not successful then show error message.
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }

    }

}
