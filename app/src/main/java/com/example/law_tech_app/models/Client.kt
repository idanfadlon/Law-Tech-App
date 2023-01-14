package com.example.law_tech_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
class Client (
    val uid:String ="",
    val fullName:String = "",
    val email:String = "",
    val phoneNumber:String = "",
    val imageURL:String = "",
    val messages : ArrayList<Message> = ArrayList()):Parcelable{
    override fun toString(): String {
        return "Client"
    }
}