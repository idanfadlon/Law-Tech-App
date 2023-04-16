package com.example.law_tech_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.adapters.MessagesListClientAdapter
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.utils.Constants
import kotlinx.android.synthetic.main.fragment_client_notifications.*


class ClientNotificationsFragment :BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_notifications, container, false)
    }
    fun loadingMessagesSuccess(messagesList:ArrayList<Message>){
        val messages = ArrayList<Message>()
        val m = Message("Family Law"
            ,"anyone",""
            ,FirestoreClass().getCurrentUserID(),
            "bla bla bla bla bla",
            true
        )
        val m1 = Message("Work Law"
            ,"anyone",""
            ,FirestoreClass().getCurrentUserID(),
            "bla bla bla bla bla",
            false
        )
        messages.add(m)
        messages.add(m1)

        for (m in messagesList){
            if (m.receiver == FirestoreClass().getCurrentUserID()){
                messages.add(m)
            }
        }
        hideProgressDialog()
        if (messages.size >0){
            rv_client_notifications.visibility = View.VISIBLE
            tv_client_notifications_noFound.visibility = View.GONE
            rv_client_notifications.layoutManager = LinearLayoutManager(activity)
            rv_client_notifications.setHasFixedSize(true)
            val messagesListClientAdapter = MessagesListClientAdapter(requireActivity(),messages)
            val dividerItemDecoration = DividerItemDecoration(rv_client_notifications.context,LinearLayoutManager.VERTICAL)
            rv_client_notifications.addItemDecoration(dividerItemDecoration)
            rv_client_notifications.adapter = messagesListClientAdapter
        }else{
            rv_client_notifications.visibility = View.GONE
            tv_client_notifications_noFound.visibility = View.VISIBLE
        }
    }
    private fun getMessagesListFromFirestore(){
        this.showProgressDialog(resources.getString(R.string.loading))
        FirestoreClass().getCurrentUserDetails(requireActivity(),Constants.CLIENTS,FirestoreClass()
            .getCurrentUserID(),this)
    }
    override fun onResume() {
        super.onResume()
        getMessagesListFromFirestore()
    }





}