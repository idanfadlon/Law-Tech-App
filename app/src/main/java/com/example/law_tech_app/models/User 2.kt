package com.example.law_tech_app.models

abstract class User {
   abstract val fullName: String
   abstract val email:String
   abstract val phoneNumber:String
   open val uid: String =""
   abstract override fun toString(): String
}