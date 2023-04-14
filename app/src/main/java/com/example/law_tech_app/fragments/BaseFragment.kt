package com.example.law_tech_app.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.law_tech_app.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.toast_layout.*
import kotlinx.android.synthetic.main.toast_layout.view.*
import java.util.*


open class BaseFragment : Fragment() {
    lateinit var mProgressDialog:Dialog
    fun showErrorSnackBar(message: String, isError: Boolean) {
        val snackBar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackBar.setTextColor(resources.getColor(R.color.primary))
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this.requireActivity(),
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this.requireActivity(),
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }
    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

}