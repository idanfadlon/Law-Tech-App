package com.example.law_tech_app.models

import kotlinx.android.parcel.Parcelize


abstract class User{
   abstract val fullName: String
   abstract val email:String
   abstract val phoneNumber:String
   abstract val imageURL:String
   open val uid: String =""
   abstract override fun toString(): String
}