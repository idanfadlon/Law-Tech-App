package com.example.law_tech_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.CategoryData
import com.example.law_tech_app.R
import com.example.law_tech_app.adapters.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_client_search_lawyer.*
import java.util.*
import kotlin.collections.ArrayList


class ClientSearchLawyerFragment :BaseFragment() {
    private var cList = ArrayList<CategoryData>()
    private lateinit var categoryAdapter:CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView =inflater.inflate(R.layout.fragment_client_search_lawyer, container, false)

        return fragView
    }

    private fun addDataToList(){
        cList.add(CategoryData("Real Estate",R.drawable.realestate))
        cList.add(CategoryData("Insurance Law",R.drawable.insuranceagent))
        cList.add(CategoryData("Family Law",R.drawable.family))
        cList.add(CategoryData("High tech",R.drawable.laptopcomputer))
        cList.add(CategoryData("Tax laws",R.drawable.tax))
        cList.add(CategoryData("Insolvencies and banking",R.drawable.bankaccount))
        cList.add(CategoryData("Litigation",R.drawable.litigation))
        cList.add(CategoryData("White Collar",R.drawable.manager))
        cList.add(CategoryData("Criminal",R.drawable.criminal))
        cList.add(CategoryData("Administrative law",R.drawable.administrativetools))
        cList.add(CategoryData("Constitutional Law",R.drawable.constitution))
        cList.add(CategoryData("Medical Malpractice",R.drawable.medicaldoctor))
        cList.add(CategoryData("Labor and Employment Law",R.drawable.labor))
        cList.add(CategoryData("Intellectual Property",R.drawable.intellectualproperty))
        cList.add(CategoryData("Privacy and data protection",R.drawable.dataprotection))
        cList.add(CategoryData("Corporate and commercial",R.drawable.b2b))

    }
    private fun loadData(){
        addDataToList()
        rv_search_lawyer.layoutManager = LinearLayoutManager(activity)
        rv_search_lawyer.setHasFixedSize(true)

        categoryAdapter = CategoryAdapter(requireActivity(),cList)

        rv_search_lawyer.adapter = categoryAdapter
        sv_search_lawyer.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sv_search_lawyer.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                sv_search_lawyer.clearFocus()

                return true
            }

        })


    }

    private fun filterList(query : String?){
        if (query != null){
            val filteredList = ArrayList<CategoryData>()
            for (category in cList){
                if(category.title.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(category)
                    Log.e("filterlist",filteredList.toString())
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(activity,"No Data Found", Toast.LENGTH_SHORT).show()
            } else{
                categoryAdapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onResume() {
        loadData()
        super.onResume()
    }
}