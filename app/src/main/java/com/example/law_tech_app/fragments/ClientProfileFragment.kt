package com.example.law_tech_app.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.BaseActivity
import com.example.law_tech_app.activities.LoginActivity
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException


open class ClientProfileFragment : BaseFragment() {
    private lateinit var tieFullName: TextInputEditText
    private lateinit var tieEmail: TextInputEditText
    private lateinit var tiePhone: TextInputEditText
    private lateinit var logoutBtn :ImageButton
    private lateinit var save :ImageButton
    private lateinit var cameraBtn: ImageButton
    private lateinit var cameraUpload: ImageButton
    lateinit var imageView: ImageView
    private var isEditable:Boolean = false
    private var isImageCaptured:Boolean = false
    private lateinit var cameraLauncher : ActivityResultLauncher<Void?>
    private lateinit var imageBitmap: Bitmap
    private lateinit var icon: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView = inflater.inflate(R.layout.fragment_client_profile, container, false)
        imageView = fragView.findViewById(R.id.iv_clientFragment_img)
        imageView.adjustViewBounds = true
        tieFullName = fragView.findViewById(R.id.tie_clientFragment_Fullname)
        tieEmail = fragView.findViewById(R.id.tie_clientFragment_Email)
        tiePhone = fragView.findViewById(R.id.tie_clientFragment_Phone)
        logoutBtn = fragView.findViewById(R.id.ib_clientFragment_logout)
        logoutBtn.setOnClickListener {
            showAlertDialog("Logout","Are you sure you want to logout ?")
        }
        cameraBtn = fragView.findViewById(R.id.btn_clientFragment_Camera)
        cameraBtn.setOnClickListener {
            cameraLauncher.launch(null)
        }
        cameraUpload = fragView.findViewById(R.id.btn_clientFragment_upload)
        cameraUpload.setOnClickListener {
            GlideLoader(requireActivity()).showImageChoosingActivity(activity as BaseActivity)
        }
        save = fragView.findViewById(R.id.ib_clientFragment_save)
        save.setOnClickListener {
            updateCurrentUserDetails()
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview(), ActivityResultCallback {
                bitmap->
                    if(bitmap != null)
                    {

                        imageBitmap = bitmap
                        imageView.setImageBitmap(bitmap)
                        isImageCaptured = true
                    }
                })
        showProgressDialog(resources.getString(R.string.loading))
            FirestoreClass().getCurrentUserDetails(requireActivity(),Constants.CLIENTS,
                FirestoreClass().getCurrentUserID(),this@ClientProfileFragment)
        return fragView
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_fragment_menu_profile,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.icon_edit_profile->{
                enableEditing()
                icon = item
                if(isEditable){
                    icon.setIcon(R.drawable.check)
                }else{
                    icon.setIcon(R.drawable.edit_profile)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun showAlertDialog(title:String, message:String){
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.warning)
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
            tiePhone.isEnabled = true
            save.isEnabled = true
        }else{
            isEditable = false
            tiePhone.isEnabled = false
            save.isEnabled = false
        }
    }
    fun loadUserDetails(currentUser:Client){
        GlideLoader(requireContext()).loadCurrentUserPicture(currentUser.imageURL,imageView)
        tieFullName.setText(currentUser.fullName)
        tieFullName.isEnabled = false
        tieEmail.setText(currentUser.email)
        tieEmail.isEnabled = false
        tiePhone.setText(currentUser.phoneNumber)
        tiePhone.isEnabled = false
        save.isEnabled = false
        cameraBtn.isEnabled = false
        cameraUpload.isEnabled = false
        this.hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
    }
    private fun  updateCurrentUserDetails(){
        if (validateUpdatingDetails())
        {
                showProgressDialog(resources.getString(R.string.loading))
                val currentUserHashMap = HashMap<String,Any>()
                if (isImageCaptured) {
                    try {
                        val baos = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                        val data = baos.toByteArray()
                        val sref: StorageReference =
                            FirebaseStorage.getInstance().reference.child("Image " + FirestoreClass().getCurrentUserID() + ".jpeg")
                        sref.putBytes(data).addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                                currentUserHashMap[Constants.IMAGE_URL] = url.toString()
                                FirestoreClass().updateCurrentUserDetails(
                                    currentUserHashMap,
                                    Constants.CLIENTS,
                                    this
                                )

                            }
                        }


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                isImageCaptured = false
                currentUserHashMap[Constants.PHONE_NUMBER] = tiePhone.text.toString().trim {it <= ' '}
                FirestoreClass().updateCurrentUserDetails(
                    currentUserHashMap,
                    Constants.CLIENTS,
                    this
                )


        }
    }
    fun updateCurrentUserDetailsSuccess(){
        isEditable = false
        tiePhone.isEnabled = false
        save.isEnabled = false
        icon.setIcon(R.drawable.edit_profile)
        hideProgressDialog()
        showErrorSnackBar("Updating details has been successfully completed",false)
    }
    private fun validateUpdatingDetails():Boolean{
        return when {
            TextUtils.isEmpty(tiePhone.text.toString().trim { it <= ' ' }) -> {
                this.hideProgressDialog()
                Toast.makeText(this.activity,"Please enter phone number",Toast.LENGTH_LONG).show()
                false
            }

            else -> {true}
        }

    }


}