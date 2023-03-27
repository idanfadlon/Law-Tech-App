package com.example.law_tech_app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.law_tech_app.R
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
//check how to update on firebase that message has been readen
class MessageDetailsActivity : BaseActivity() {
    lateinit var currentMessage:com.example.law_tech_app.models.Message
    lateinit var senderImage:ImageView
    lateinit var subject:TextView
    lateinit var sender:TextView
    lateinit var description:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)
        if (intent.hasExtra(Constants.EXTRA_MESSAGE_DETAILS)){
            currentMessage = intent.extras?.get(Constants.EXTRA_MESSAGE_DETAILS)!! as com.example.law_tech_app.models.Message
        }
        senderImage = findViewById(R.id.iv_profileFragment_lawyer)
        subject = findViewById(R.id.tv_messageDetailsActivity_subject)
        sender = findViewById(R.id.tv_messageDetailsActivity_sender)
        description = findViewById(R.id.tv_messageDetailsActivity_status)

        loadCurrentMessageDetails()
    }
    @SuppressLint("SetTextI18n")
    fun loadCurrentMessageDetails(){
        this.showProgressDialog(resources.getString(R.string.loading))
        GlideLoader(this).loadCurrentUserPicture(currentMessage.senderImageURL,senderImage)
        subject.text =subject.text.toString() + " " +currentMessage.subject
        sender.text =sender.text.toString() + " " +currentMessage.sender
        description.text = description.text.toString() +" "+ currentMessage.body
        hideProgressDialog()
    }
    override fun onResume() {
        super.onResume()

    }
}