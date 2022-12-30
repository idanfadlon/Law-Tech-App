package com.example.law_tech_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.LawyerMainActivity
import com.example.law_tech_app.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class LawyerProfileFragment : Fragment() {
    lateinit var logoutBtn :Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lawyer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logoutBtn = view.findViewById(R.id.btn_lawyer_profileFragment)
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this.activity,LoginActivity::class.java)
            startActivity(intent)
            this.activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}