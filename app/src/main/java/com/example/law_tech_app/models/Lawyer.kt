package com.example.law_tech_app.models

import android.os.Parcelable
import io.grpc.internal.DnsNameResolver.SrvRecord
import kotlinx.android.parcel.Parcelize

@Parcelize
class Lawyer (
      val uid:String,
      val fullName:String,
      val email:String,
      val licenseNumber:String,
      val phoneNumber:String,
      val aboutMe:String,
      val blockedClients: ArrayList<String> = ArrayList(),
      val messages : ArrayList<Message> = ArrayList()
     ):Parcelable
