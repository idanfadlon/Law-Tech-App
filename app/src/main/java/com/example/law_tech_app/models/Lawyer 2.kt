package com.example.law_tech_app.models

import android.os.Parcelable
import com.example.law_tech_app.utils.Constants
import io.grpc.internal.DnsNameResolver.SrvRecord
import kotlinx.android.parcel.Parcelize
import java.util.Objects

@Parcelize
open class Lawyer (
    override val uid:String ="",
    override val fullName:String = "",
    override val email:String = "",
    val licenseNumber:String = "",
    val specialization :String = "",
    override val phoneNumber:String = "",
    val aboutMe:String = "",
    val imageURL:String = "",
    val blockedClients: ArrayList<String> = ArrayList(),
    val messages : ArrayList<Message> = ArrayList()

):User(),Parcelable
{
    //represent the collection this object belong to
    override fun toString(): String {
        return "lawyers"
    }

}


//      constructor(uid :String,fullName:String,email:String,licenseNumber:String,
//                  specialization :String,phoneNumber:String,aboutMe:String ) : this()
//      {
//        this.uid = uid
//        this.fullName = fullName
//        this.email = email
//        this.licenseNumber = licenseNumber
//        this.specialization = specialization
//        this.phoneNumber = phoneNumber
//        this.aboutMe = aboutMe
//        this.imageURL = ""
//        this.blockedClients = ArrayList()
//        this.messages = ArrayList()
//      }
//      fun toJson():HashMap<String,Any>{
//          val json = HashMap<String,Any>()
//          json[Constants.UID] = uid
//          json[Constants.FULL_NAME] = fullName
//          json[Constants.EMAIL] = email
//          json[Constants.LICENSE_NUMBER] = licenseNumber
//          json[Constants.SPECIALIZATION] = specialization
//          json[Constants.PHONE_NUMBER] = phoneNumber
//          json[Constants.ABOUT_ME] = aboutMe
//          json[Constants.IMAGE_URL] = imageURL
//          json[Constants.BLOCKED_CLIENTS] = blockedClients
//          json[Constants.MESSAGES] = messages
//          return json
//      }





