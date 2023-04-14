package com.example.law_tech_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.law_tech_app.R

class AddMessageActivity : BaseActivity() {
    lateinit var subject:String
    lateinit var senderUID:String
    lateinit var senderFullname:String
    lateinit var senderImageURL:String
    lateinit var receiver: String
    lateinit var messageBody:String
    var isReaden:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_message)
    }
}