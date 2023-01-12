package com.example.law_tech_app.models

import android.content.BroadcastReceiver
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Message(
    val subject:String,
    val sender:String,
    val senderImageURL:String,
    val receiver: String,
    val body:String,
    var isReaden:Boolean
):Parcelable{
    companion object{
        var messageId = 0
        fun getMessageID(): Int {return messageId}
    }
    override fun toString(): String {
        return "messages"
    }

}