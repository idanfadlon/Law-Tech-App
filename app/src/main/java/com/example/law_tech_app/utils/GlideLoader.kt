package com.example.law_tech_app.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.BaseActivity
import java.io.IOException


class GlideLoader(val context: Context) {

    fun loadCurrentUserPicture(imageURL:String,image:ImageView){
        if(imageURL != "") {
            Glide.with(context).load(imageURL).centerCrop()
                .placeholder(R.drawable.imageprofile)
                .into(image)
        }else{
            Glide.with(context).load(R.drawable.ic_launcher_round).centerCrop()
                .placeholder(R.drawable.imageprofile)
                .into(image)
        }
    }
    fun loadPicture(image: Any, imageView: ImageView) {
        if(image != "")
        {
            try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .into(imageView) // the view in which the image will be loaded.
            } catch (e: IOException) {
            e.printStackTrace()
            }
        }else{
            try {
                // Load the user image in the ImageView.
                Glide
                    .with(context)
                    .load(R.drawable.ic_launcher_round) // Uri or URL of the image
                    .centerCrop() // Scale type of the image.
                    .into(imageView) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }

    fun showImageChoosingActivity(activity:BaseActivity){
        val galleyActivity = Intent(Intent.ACTION_PICK
        ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleyActivity,Constants.IMAGE_UPLOAD_CODE)
    }

}