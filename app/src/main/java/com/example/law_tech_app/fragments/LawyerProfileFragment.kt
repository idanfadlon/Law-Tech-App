package com.example.law_tech_app.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.BaseActivity
import com.example.law_tech_app.activities.LoginActivity
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

/*
check how to refresh the fragment in order to see immediately changing picture
check how to implement the camera opening and taking picture
*/

open class LawyerProfileFragment : BaseFragment() {
    private lateinit var tieFullName: TextInputEditText
    private lateinit var tieEmail: TextInputEditText
    private lateinit var tieLicense: TextInputEditText
    private lateinit var tieSpecial: TextInputEditText
    private lateinit var tiePhone: TextInputEditText
    private lateinit var tieAbout: TextInputEditText
    private lateinit var logoutBtn :Button
    private lateinit var save :Button
    private lateinit var cameraBtn: ImageButton
    private lateinit var cameraUpload: ImageButton
      lateinit var imageView: ImageView
     private lateinit var editTV: TextView
     private var isEditable:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView = inflater.inflate(R.layout.fragment_lawyer_profile, container, false)
        imageView = fragView.findViewById(R.id.iv_messageDetailsActivity_sender)
        imageView.adjustViewBounds = true
        tieFullName = fragView.findViewById(R.id.tie_lawyerFragment_Fullname)
        tieEmail = fragView.findViewById(R.id.tie_lawyerFragment_Email)
        tieLicense = fragView.findViewById(R.id.tie_lawyerFragment_License)
        tieSpecial = fragView.findViewById(R.id.tie_lawyerFragment_Speical)
        tiePhone = fragView.findViewById(R.id.tie_lawyerFragment_Phone)
        tieAbout = fragView.findViewById(R.id.tie_lawyerFragment_About)
        logoutBtn = fragView.findViewById(R.id.btn_lawyer_profileFragment)
        logoutBtn.setOnClickListener {
            showAlertDialog("Logout","Are you sure you want to logout ?")
        }
        cameraBtn = fragView.findViewById(R.id.btn_lawyerFragment_Camera)
        cameraBtn.setOnClickListener {
            //TODO implement the camera opening
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.activity?.startActivityForResult(cameraIntent,Constants.IMAGE_REQUEST_CODE)
        }
        cameraUpload = fragView.findViewById(R.id.btn_lawyerFragment_upload)
        cameraUpload.setOnClickListener {
            GlideLoader(requireActivity()).showImageChoosingActivity(activity as BaseActivity)
        }
        save = fragView.findViewById(R.id.btn_profileFragment_save)
        save.setOnClickListener {
            this.showProgressDialog(resources.getString(R.string.loading))
            updateCurrentUserDetails()
        }
        editTV = fragView.findViewById(R.id.tv_profileFragment_edit)
        editTV.setOnClickListener {
            enableEditing()
        }
        return fragView
    }
    private fun showAlertDialog(title:String, message:String){
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.ic_baseline_warning_24)
        dialogBuilder.setPositiveButton(R.string.yes){
                _, _ ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        dialogBuilder.setNegativeButton(R.string.no){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    private fun enableEditing(){
        if(!isEditable) {
            isEditable = true
            tieLicense.isEnabled = true
            tieSpecial.isEnabled = true
            tiePhone.isEnabled = true
            tieAbout.isEnabled = true
            save.isEnabled = true
        }else{
            isEditable = false
            tieLicense.isEnabled = false
            tieSpecial.isEnabled = false
            tiePhone.isEnabled = false
            tieAbout.isEnabled = false
            save.isEnabled = false
        }
    }
    fun loadUserDetails(currentUser:Lawyer){
        GlideLoader(requireContext()).loadCurrentUserPicture(currentUser.imageURL,imageView)
        tieFullName.setText(currentUser.fullName)
        tieFullName.isEnabled = false
        tieEmail.setText(currentUser.email)
        tieEmail.isEnabled = false
        tieLicense.setText(currentUser.licenseNumber)
        tieLicense.isEnabled = false
        tieSpecial.setText(currentUser.specialization)
        tieSpecial.isEnabled = false
        tiePhone.setText(currentUser.phoneNumber)
        tiePhone.isEnabled = false
        tieAbout.setText(currentUser.aboutMe)
        tieAbout.isEnabled = false
        save.isEnabled = false
        this.hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        Log.e("check","inside onResume")
        this.showProgressDialog(resources.getString(R.string.loading))
        FirestoreClass().getCurrentUserDetails(requireActivity(),Constants.LAWYERS,
            FirestoreClass().getCurrentUserID(),this@LawyerProfileFragment)
    }
    private fun updateCurrentUserDetails(){
        if (isEditable && validateUpdatingDetails()){

            val userHashMap :HashMap<String,Any> = HashMap()
            userHashMap[Constants.LICENSE_NUMBER] = tieLicense.text.toString().trim {it <= ' '}
            userHashMap[Constants.SPECIALIZATION] = tieSpecial.text.toString().trim {it <= ' '}
            userHashMap[Constants.PHONE_NUMBER] = tiePhone.text.toString().trim {it <= ' '}
            userHashMap[Constants.ABOUT_ME] = tieAbout.text.toString().trim {it <= ' '}
            FirestoreClass().updateCurrentUserDetails(userHashMap,Constants.LAWYERS)
            updateCurrentUserDetailsSuccess()
        }
    }
    private fun updateCurrentUserDetailsSuccess(){
        isEditable = false
        tieLicense.isEnabled = false
        tieSpecial.isEnabled = false
        tiePhone.isEnabled = false
        tieAbout.isEnabled = false
        save.isEnabled = false
        this.hideProgressDialog()
        Toast.makeText(activity,"Updating details has been successfully completed",Toast.LENGTH_LONG).show()
    }
    private fun validateUpdatingDetails():Boolean{
        return when {
            TextUtils.isEmpty(tiePhone.text.toString().trim { it <= ' ' }) -> {
                this.hideProgressDialog()
                Toast.makeText(this.activity,"Please enter phone number",Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(tieLicense.text.toString().trim { it<=' ' })->{
                this.hideProgressDialog()
                Toast.makeText(this.activity,"Please enter license number",Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(tieSpecial.text.toString().trim { it<=' ' })->{
                this.hideProgressDialog()
                Toast.makeText(this.activity,"Please enter specialization",Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(tieAbout.text.toString().trim(){ it<=' '})->{
                this.hideProgressDialog()
                Toast.makeText(this.activity,"Please tell a little about yourself",Toast.LENGTH_LONG).show()
                false
            }

            else -> {true}
        }

    }


}