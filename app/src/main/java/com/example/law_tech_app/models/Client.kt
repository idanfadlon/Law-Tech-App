package com.example.law_tech_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
class Client (
   override val uid:String ="",
    val fullName:String = "",
    val email:String = "",
    val phoneNumber:String = "",
    val imageURL:String = "",
    val messages : ArrayList<Message> = ArrayList()
):User(),Parcelable{
    override fun toString(): String {
        return "clients"
    }
}