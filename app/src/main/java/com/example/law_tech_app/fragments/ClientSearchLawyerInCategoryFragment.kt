package com.example.law_tech_app.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.CategoryData
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.LawyerData
import com.example.law_tech_app.R
import com.example.law_tech_app.adapters.CategoryAdapter
import com.example.law_tech_app.adapters.LawyerDataAdapter
import com.example.law_tech_app.databinding.SpecializationItemBinding
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.utils.Constants
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_client_search_lawyer.*
import kotlinx.android.synthetic.main.fragment_client_search_lawyer_in_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList


class ClientSearchLawyerInCategoryFragment :BaseFragment() {
    private var lawyersList = ArrayList<LawyerData>()
    private lateinit var lawyersAdapter:LawyerDataAdapter
    lateinit var currentUser:Client


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView =inflater.inflate(R.layout.fragment_client_search_lawyer_in_category, container, false)

        return fragView
    }

    private fun addDataToList(specialization: String){
        val firebase = FirestoreClass()
        val lawyersCollection = firebase.getCollectionData("/lawyers")
        val query = lawyersCollection.whereEqualTo("specialization", specialization)

        query.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                for (document in task.result) {
                    val lawyerName = document.getString("fullName")
                    val lawyerImage = document.getString("imageURL")
                    val lawyerAbout = document.getString("aboutMe")
                    val lawyerEmail = document.getString("email")
                    val lawyerId = document.id
                    if(lawyersList.contains(LawyerData(lawyerName, lawyerImage, lawyerAbout,lawyerEmail,lawyerId)))
                        continue
                    else {
                        lawyersList.add(LawyerData(lawyerName, lawyerImage, lawyerAbout,lawyerEmail,lawyerId))
                    }
                }

                rv_search_lawyer_in_category.layoutManager = LinearLayoutManager(activity)
                rv_search_lawyer_in_category.setHasFixedSize(true)
                lawyersAdapter = LawyerDataAdapter(requireActivity(),lawyersList, currentUser,this)

                rv_search_lawyer_in_category.adapter = lawyersAdapter
                sv_search_lawyer_in_category.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText)

                        return true
                    }

                })

            }
            else{
                Log.e("task fail","task fail")
            }


        }

    }


    fun loadingUserDetails(currentuser:Client){
        currentUser = currentuser
    }
    fun loadData(specialization: String){
        addDataToList(specialization) //TODO: get from cardView

    }

    private fun filterList(query : String?){
        if (query != null){
            val filteredList = ArrayList<LawyerData>()
            for (lawyer in lawyersList){
                if(lawyer.name?.lowercase(Locale.ROOT)?.contains(query) == true){
                    filteredList.add(lawyer)
                    Log.e("filterlist",filteredList.toString())
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(activity,"No Data Found", Toast.LENGTH_SHORT).show()
            } else{
                lawyersAdapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onResume() {
//        val category = arguments?.getString("lawyerCategory")
        super.onResume()
       FirestoreClass().getCurrentUserDetails(requireActivity(),Constants.CLIENTS,FirestoreClass().getCurrentUserID(),this)
        loadData("Criminal")

    }
}