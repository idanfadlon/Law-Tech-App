package com.example.law_tech_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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
    lateinit var reply:TextView
    lateinit var replyBtn:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)
        if (intent.hasExtra(Constants.EXTRA_MESSAGE_DETAILS)){
            currentMessage = intent.extras?.get(Constants.EXTRA_MESSAGE_DETAILS)!! as com.example.law_tech_app.models.Message
        }
        senderImage = findViewById(R.id.iv_messageDetails_img)
        subject = findViewById(R.id.tv_messageDetailsActivity_subject)
        sender = findViewById(R.id.tv_messageDetailsActivity_sender)
        description = findViewById(R.id.tv_messageDetailsActivity_time)
        reply = findViewById(R.id.tv_messageDetailsActivity_reply)
        replyBtn = findViewById(R.id.ib_messageDetailsActivity_reply)
        replyBtn.setOnClickListener {
            val intent = Intent(this,AddMessageActivity::class.java)
            intent.putExtra(Constants.EXTRA_MESSAGE_DETAILS,currentMessage)
            startActivity(intent)
        }
        loadCurrentMessageDetails()
    }


    private fun loadCurrentMessageDetails(){
        this.showProgressDialog(resources.getString(R.string.loading))
        GlideLoader(this).loadCurrentUserPicture(currentMessage.senderImageURL,senderImage)
        subject.text =subject.text.toString() + " " +currentMessage.subject
        sender.text =sender.text.toString() + " " +currentMessage.senderFullname
        description.text = description.text.toString() +" "+ currentMessage.messageBody
        reply.text = "Reply to " + currentMessage.senderFullname
        hideProgressDialog()
    }
    override fun onResume() {
        super.onResume()

    }
}