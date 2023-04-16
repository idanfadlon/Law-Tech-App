package com.example.law_tech_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
open class Client (
    override val uid:String ="",
    override val fullName:String = "",
    override val email:String = "",
    override val phoneNumber:String = "",
    val imageURL:String = "",
    val messages : ArrayList<Message> = ArrayList()
):User(),Parcelable{
    override fun toString(): String {
        return "clients"
    }
}