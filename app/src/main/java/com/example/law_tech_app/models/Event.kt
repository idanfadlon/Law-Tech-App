package com.example.law_tech_app.models

import android.hardware.ConsumerIrManager.CarrierFrequencyRange
import android.os.Parcelable
import com.bumptech.glide.Priority
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
class Event(
    val id:String,
    val owner:String,
    val participants:String,
    val subject:String,
    val eventDay: Int,
    val eventMonth:Int,
    val priority:String,
    val duration: Double,
    val description:String,
    var type:String,
    val frequency:String
): Parcelable{
    constructor() :this ("","","","",0,0,""
    ,0.0,"","","")
}