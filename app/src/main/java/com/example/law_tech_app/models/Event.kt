package com.example.law_tech_app.models

import android.hardware.ConsumerIrManager.CarrierFrequencyRange
import android.os.Parcelable
import com.bumptech.glide.Priority
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
class Event(
    val id:String,
    val owner:String,
    var participants:String,
    var subject:String,
    val eventDay: Int,
    val eventMonth:Int,
    var priority:String,
    var duration: Double,
    var description:String,
    var type:String,
    var preparingDuration:Double,
    var time:String
): Parcelable{
    constructor() :this ("","","","",0,0,""
    ,0.0,"","",0.0,"")
}