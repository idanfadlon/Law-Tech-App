package com.example.law_tech_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.adapters.MessagesListAdapter
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.utils.Constants
import java.util.*
import kotlin.collections.ArrayList


class LawyerNotificationsFragment :BaseFragment() {
    lateinit var currentUser: Lawyer
    lateinit var recyclerView: RecyclerView
    lateinit var tvNoFound:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView =  inflater.inflate(R.layout.fragment_lawyer_notifications, container, false)
        recyclerView = fragView.findViewById(R.id.rv_lawyer_notifications)
        tvNoFound = fragView.findViewById(R.id.tv_lawyer_notifications_noFound)
        return fragView
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

        if (messages.size > 0){
            recyclerView.visibility = View.VISIBLE
            tvNoFound.visibility = View.GONE
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            val messagesListAdapter = MessagesListAdapter(requireActivity(),messages,currentUser,this)
            recyclerView.adapter = messagesListAdapter
        }else{
            recyclerView.visibility = View.GONE
            tvNoFound.visibility = View.VISIBLE
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