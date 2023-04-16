package com.example.law_tech_app.Firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.law_tech_app.activities.ClientMainActivity
import com.example.law_tech_app.activities.LawyerMainActivity
import com.example.law_tech_app.activities.LoginActivity
import com.example.law_tech_app.activities.SignUpActivity
import com.example.law_tech_app.fragments.*
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


///**
// * A custom class where we will add the operation performed for the FireStore database.
// */
class FirestoreClass {

    // Access a Cloud Firestore instance.
     val mFireStore = Firebase.firestore

//
//    /**
//     * A function to make an entry of the registered user in the FireStore database.
//     */
    fun registerUser(activity: SignUpActivity,UserInfo: User) {

    // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(UserInfo.toString())
            // Document ID for users fields. Here the document it is the User ID.
            .document(UserInfo.uid)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead   replacing the fields.
            .set(UserInfo,SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
              if (UserInfo.toString() == Constants.LAWYERS)
                  activity.lawyerSignUpSuccess()
                else activity.clientSignUpSuccess()

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
    fun getCurrentUserDetails(activity: Activity, collectionName:String, uid:String, fragment: Fragment? = null) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(collectionName)
            // The document id to get the Fields of user.
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                // Here we have received the document snapshot which is converted into the User Data model object.
                val currentUser:User? = if (document.exists())
                {
                    when (collectionName) {
                        Constants.LAWYERS -> {
                            document.toObject(Lawyer::class.java)!!
                        }
                        else -> {
                            document.toObject(Client::class.java)!!
                        }
                    }
                }else {
                    null
                }
                when(activity)
                {
                    is LoginActivity->
                    {
                        val sharedPreferences =
                            activity.getSharedPreferences(
                                Constants.LAWTECH_PREFERENCES,
                                Context.MODE_PRIVATE
                            )
                        // Create an instance of the editor which is help us to edit the SharedPreference.
                        val editor
                                : SharedPreferences.Editor = sharedPreferences.edit()
                        if (currentUser != null) {
                            editor.putString(
                                Constants.CURRENT_USERNAME,
                                currentUser.fullName
                            )
                        }
                        editor.apply()
                        activity.userLoggedInSuccess(currentUser)
                    }
                    is LawyerMainActivity->{
                        if (fragment != null && (fragment is LawyerProfileFragment)){
                            fragment.loadUserDetails(currentUser as Lawyer)
                        }
                        if (fragment !=null && (fragment is LawyerNotificationsFragment)){
                            fragment.loadingMessagesSuccess((currentUser as Lawyer).messages)
                        }
                    }

                    is ClientMainActivity ->{
                        if (fragment != null && (fragment is ClientProfileFragment)){
                            fragment.loadUserDetails(currentUser as Client)
                        }
                        if (fragment !=null && (fragment is ClientNotificationsFragment)){
                            fragment.loadingMessagesSuccess((currentUser as Client).messages)
                        }
                    }
                }

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is LawyerMainActivity->{
                        if (fragment != null && fragment is BaseFragment){
                            fragment.hideProgressDialog()
                        }
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
   }

    fun updateCurrentUserDetails(currentUserHashMap:HashMap<String,Any>,collection:String){
        mFireStore.collection(collection)
            .document(getCurrentUserID()).update(currentUserHashMap)
            .addOnSuccessListener {

            }.addOnFailureListener{

            }


    }
    fun getCollectionData(collectionName: String): CollectionReference {
        return mFireStore.collection(collectionName)

    }}