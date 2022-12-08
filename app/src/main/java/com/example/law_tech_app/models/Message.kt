package com.example.law_tech_app.models

import android.content.BroadcastReceiver
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message(
    private val subject:String,
    private val sender:String,
    private val receiver: String,
    private val body:String,
):Parcelable