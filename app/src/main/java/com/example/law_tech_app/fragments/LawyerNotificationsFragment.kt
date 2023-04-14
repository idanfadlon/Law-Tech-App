package com.example.law_tech_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.adapters.MessagesListAdapter
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.utils.Constants
import kotlinx.android.synthetic.main.fragment_lawyer_notifications.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LawyerNotificationsFragment :BaseFragment() {
    lateinit var currentUser: Lawyer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lawyer_notifications, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    fun loadingUserDetails(currentuser: Lawyer){
        currentUser = currentuser
        FirestoreClass().getNotificationsFromFirestore(this@LawyerNotificationsFragment)
    }

    fun blockUserSuccess(){
        hideProgressDialog()
        showErrorSnackBar("User was successfully blocked !",false)
    }
    fun deleteMessageSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Message was successfully deleted !",false)
    }
    fun loadingMessagesSuccess(messagesList:ArrayList<Message>){
        val messages = ArrayList<Message>()
        for(msg in messagesList){
            if(!currentUser.blockedClients.contains(msg.senderUID)){
                messages.add(msg)
            }
        }
        messages.add(
            Message(

            )
        )

        if (messages.size > 0){
            rv_lawyer_notifications.visibility = View.VISIBLE
            tv_lawyer_notifications_noFound.visibility = View.GONE
            rv_lawyer_notifications.layoutManager = LinearLayoutManager(activity)
            rv_lawyer_notifications.recycledViewPool.setMaxRecycledViews(0,0)
            rv_lawyer_notifications.setHasFixedSize(true)
            val messagesListAdapter = MessagesListAdapter(requireActivity(),messages,currentUser,this)
            rv_lawyer_notifications.adapter = messagesListAdapter
        }else{
            rv_lawyer_notifications.visibility = View.GONE
            tv_lawyer_notifications_noFound.visibility = View.VISIBLE
        }
        hideProgressDialog()
    }
    fun addMessageSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Message was sent successfully",false)

    }

    override fun onResume() {
        super.onResume()
        showProgressDialog(resources.getString(R.string.loading))
        FirestoreClass().getCurrentUserDetails(requireActivity(),Constants.LAWYERS,FirestoreClass()
            .getCurrentUserID(),this)
    }

}