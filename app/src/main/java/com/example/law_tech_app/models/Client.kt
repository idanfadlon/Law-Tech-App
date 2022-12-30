package com.example.law_tech_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Client (


):User(),Parcelable{
    override fun toString(): String {
        return "Client"
    }
}