package com.example.law_tech_app.models

import android.os.Parcelable


import kotlinx.parcelize.Parcelize
@Parcelize
open class Client (
    override var uid:String ="",
    override val fullName:String = "",
    override val email:String = "",
    override val phoneNumber:String = "",
    override val imageURL:String = "",
    val messages : ArrayList<Message> = ArrayList()
):User(),Parcelable{
    override fun toString(): String {
        return "clients"
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}