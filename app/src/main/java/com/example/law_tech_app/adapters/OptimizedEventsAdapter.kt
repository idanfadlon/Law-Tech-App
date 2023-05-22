package com.example.law_tech_app.adapters

import android.app.AlertDialog
import android.app.Dialog
import com.example.law_tech_app.models.Event
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class OptimizedEventsAdapter(
    val context: Context,
    val eventsCombinationsList:ArrayList<ArrayList<Event>>,
    val allEvents:ArrayList<Event>,
    val fragment: LawyerCalendarFragment,
    val optimizationDialog: Dialog
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventsForDelete = ArrayList<Event>()
    private val eventsTimes = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessagesListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.events_optimization_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return eventsCombinationsList.size
    }
    private fun showAlertDialog(events:ArrayList<Event>){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("  Events Optimization")
        var allevents = ""
        events.forEach {
            allevents += if(it != events.last()) it.subject + " ,"
            else {
                it.subject
            }
        }
        dialogBuilder.setMessage(
            "Are you sure you want to perform these events ? \n" + allevents
        )
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.optimize_warning)
        dialogBuilder.setPositiveButton("Yes, optimize"){
                dialog, _ ->
            fragment.showProgressDialog("Optimizing..")
            dialog.cancel()
            optimizationDialog.dismiss()
            allEvents.forEach {
                eventsTimes.add(it.time)
                if (!events.contains(it)){
                    eventsForDelete.add(it)
                }
            }
            eventsForDelete.forEach {
                eventsTimes.remove(it.time)
                allEvents.remove(it)
            }
            val eventsAfterTimeSorting = events.sortedBy { item ->
                val index = allEvents.indexOf(item)
                if (index != -1) index else Int.MAX_VALUE // Handle IndexOutOfBoundsException
            }

            val finalEvents = ArrayList<Event>(eventsAfterTimeSorting)
            runBlocking {
                launch {
                    FirestoreClass().updateEventsAfterOptimization(eventsTimes,finalEvents)
                }
            }

            runBlocking {
                launch {
                   async { FirestoreClass().deleteEventsAfterOptimization(eventsForDelete, fragment)}
                }
            }
            eventsTimes.clear()
            eventsForDelete.clear()
        }
        dialogBuilder.setNegativeButton("No thanks"){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val events = eventsCombinationsList[position]
        val tvEvents = holder.itemView.findViewById<TextView>(R.id.tv_optimization_events_list)
        events.forEach {
            if(it != events.last()){tvEvents.text = tvEvents.text.toString() + it.subject +  " ,"}
            else {tvEvents.text = tvEvents.text.toString() + it.subject}
        }
        holder.itemView.setOnClickListener {
            showAlertDialog(events)
        }
    }
}