package com.example.law_tech_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
class Event (
    val uid:String,
    val owner:String,
    val participants :ArrayList<String> = ArrayList(),
    val subject:String,
    val date:String
): Parcelable