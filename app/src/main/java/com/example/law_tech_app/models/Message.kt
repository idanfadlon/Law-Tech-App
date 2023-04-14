package com.example.law_tech_app.models

import android.content.BroadcastReceiver
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class Message(
    var id:String = "",
    val subject:String,
    val senderUID:String,
    val senderFullname:String,
    val senderImageURL:String,
    val receiver: String,
    val messageBody:String,
    val date:String,
    var isReaden:Boolean
):Parcelable{
    constructor() : this("", "", "","matan ohayon","",
    "","", Calendar.getInstance().time.toString(),false)


}