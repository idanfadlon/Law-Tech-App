package com.example.law_tech_app.Firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import com.example.law_tech_app.activities.SignUpActivity
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

//
//import android.app.Activity
//import android.content.Context
//import android.content.SharedPreferences
//import android.util.Log
//import com.example.law_tech_app.activities.LoginActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.SetOptions
//import com.myshoppal.models.User
//import com.myshoppal.ui.activities.RegisterActivity
//import com.myshoppal.utils.Constants
//
///**
// * A custom class where we will add the operation performed for the FireStore database.
// */
class FirestoreClass {

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()
//
//    /**
//     * A function to make an entry of the registered user in the FireStore database.
//     */
    fun registerLawyer(activity: SignUpActivity,LawyerInfo:Lawyer) {

    // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.LAWYERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(LawyerInfo.uid)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead   replacing the fields.
            .set(LawyerInfo,SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.lawyerSignUpSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }
    //
//    /**
//     * A function to get the user id of current logged user.
//     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
//
//    /**
//     * A function to get the logged user details from from FireStore Database.
//     */
//    fun getCurrentUserDetails(activity: Activity,collectionName :String) {
//
//        // Here we pass the collection name from which we wants the data.
//        mFireStore.collection(collectionName)
//            // The document id to get the Fields of user.
//            .document(getCurrentUserID())
//            .get()
//            .addOnSuccessListener { document ->
//
//                // Here we have received the document snapshot which is converted into the User Data model object.
//                lateinit var currentUser :Any
//                if (collectionName.equals(Constants.LAWYERS)) {
//                    currentUser = document.toObject(Lawyer::class.java)!!
//                }else{
//                    currentUser = document.toObject(Client::class.java)!!
//                }
//
//                val sharedPreferences =
//                    activity.getSharedPreferences(
//                        Constants.LAWTECH_PREFERENCES,
//                        Context.MODE_PRIVATE
//                    )
//
//                // Create an instance of the editor which is help us to edit the SharedPreference.
//                val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                editor.putString(
//                    Constants.CURRENT_USERNAME,
//                    "${}"
//                )
//                editor.apply()
//
//                when (activity) {
//                    is LoginActivity -> {
//                        // Call a function of base activity for transferring the result to it.
//                        activity.userLoggedInSuccess(user)
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                // Hide the progress dialog if there is any error. And print the error in log.
//                when (activity) {
//                    is LoginActivity -> {
//                        activity.hideProgressDialog()
//                    }
//                }
//
//                Log.e(
//                    activity.javaClass.simpleName,
//                    "Error while getting user details.",
//                    e
//                )
//            }
//    }
}