package com.example.law_tech_app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.AddEventActivity
import com.example.law_tech_app.adapters.EventListAdapter
import com.example.law_tech_app.adapters.OptimizedEventsAdapter
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.EventsOptimization
import com.example.law_tech_app.utils.GlideLoader
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class LawyerCalendarFragment :BaseFragment() {
    private lateinit var calendar:CalendarView
    private lateinit var events:ArrayList<Event>
    private lateinit var addEvent:FloatingActionButton
    private var currentCalendarDay = 0
    private var currentCalendarMonth = 0
    lateinit var recyclerView: RecyclerView
    lateinit var tvNoFound:TextView
    var combinations:ArrayList<ArrayList<Double>> = ArrayList()
    private var combinationsOfSortedEventsLists = ArrayList<ArrayList<Event>>()
    private lateinit var optimizedEventsDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lawyer_fragment_menu_calendar,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.icon_optimize ->{
                showAlertDialog1()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_lawyer_calendar, container, false)
        calendar = fragView.findViewById(R.id.calendar_lawyerFragment)
        calendar.setOnDateChangeListener{ calendar,year,month,day->
            currentCalendarDay = day
            currentCalendarMonth = month + 1
            FirestoreClass().getEventsFromFirestore(this, day, month + 1)
//            if (selectedCalendarDateInRange(year, month+1, day)) {
//                addEvent.isEnabled = true
//                currentCalendarDay = day
//                currentCalendarMonth = month + 1
//                FirestoreClass().getEventsFromFirestore(this, day, month + 1)
//            }
//            else{
//                addEvent.isEnabled  = false
//            }
//            val calendar = Calendar.getInstance()
//            calendar.set(year,month,day)
//            Log.e("matan",calendar.get(Calendar.WEEK_OF_MONTH).toString())

        }

        currentCalendarMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
        currentCalendarDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        addEvent = fragView.findViewById(R.id.fb_lawyerFragment_calendar)
        addEvent.setOnClickListener{

            val intent = Intent(activity, AddEventActivity::class.java)
            val array = ArrayList<Int>()
            array.add(currentCalendarDay)
            array.add(currentCalendarMonth)
            intent.putExtra(Constants.EXTRA_EVENT_DETAILS,array)
            startActivity(intent)
        }
        recyclerView = fragView.findViewById(R.id.rv_lawyerFragment_meetings)
        tvNoFound = fragView.findViewById(R.id.tv_lawyerFragment_meetings_noEvents)
        return fragView
    }
    private fun selectedCalendarDateInRange(year:Int, month:Int, day:Int) : Boolean{
        val totalSelected = (year*360) + (month*30) + day
        val totalCurrent = ((Calendar.getInstance().get(Calendar.YEAR))*360) +
                (((Calendar.getInstance().get(Calendar.MONTH))+1)*30) + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        return ((totalSelected - totalCurrent) <= 14 && (totalSelected - totalCurrent) >= 0)

    }
    fun deleteEventSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Event was successfully canceled !",false)
    }
    fun updateEventSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Event was successfully updated !",false)
    }
    fun loadingEventsSuccess(eventsList:ArrayList<Event>){
        events= ArrayList()
        val list = eventsList.sortedWith(compareBy { it.time })
        list.forEach { events.add(it) }
        hideProgressDialog()
        if (eventsList.size > 0){
            recyclerView.visibility = View.VISIBLE
            tvNoFound.visibility = View.GONE
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            val eventDetailsActivityAdapter = EventListAdapter(requireActivity(),events,this)
            recyclerView.adapter = eventDetailsActivityAdapter

        }else{
            recyclerView.visibility = View.GONE
            tvNoFound.visibility = View.VISIBLE
        }
    }

    private fun getEventsListFromFirestore(){
        showProgressDialog(resources.getString(R.string.loading))
        FirestoreClass().getEventsFromFirestore(this,currentCalendarDay,currentCalendarMonth)

    }
    override fun onResume() {
        super.onResume()
        getEventsListFromFirestore()
    }

    private fun createOptimizedEventsDialog(combinationsOfSortedEventsLists:ArrayList<ArrayList<Event>>){
        optimizedEventsDialog = Dialog(requireActivity())
        optimizedEventsDialog.setContentView(R.layout.dialog_optimization_events)
        val recyclerView = optimizedEventsDialog.findViewById<RecyclerView>(R.id.rv_dialog_optimization)
        recyclerView.layoutManager = LinearLayoutManager(optimizedEventsDialog.context)
        val adapter = OptimizedEventsAdapter(optimizedEventsDialog.context,combinationsOfSortedEventsLists,events,this,optimizedEventsDialog)
        recyclerView.adapter = adapter
        optimizedEventsDialog.setCancelable(true)
        optimizedEventsDialog.setCanceledOnTouchOutside(true)
        optimizedEventsDialog.show()

    }
    private fun showAlertDialog2(){
        val dialogBuilder = AlertDialog.Builder(context)
        val eventsDurations = ArrayList<Double>()
        events.forEach { eventsDurations.add(it.duration) }
        val maxEvents = EventsOptimization().calculateMaximumCompletedTasks(events.size,480.0,eventsDurations,events)
        if(maxEvents == 0){
            showErrorSnackBar("There is no events to optimize yet !",true)
            return
        }
        dialogBuilder.setTitle("  Events Optimization")
        dialogBuilder.setMessage(
            "The maximum number of events which can be completed on $currentCalendarDay/$currentCalendarMonth is: $maxEvents\n\n" +
                    "* Considering any preparation duration of each event *\n\n" +
                    "Are you sure you want to optimize your events on $currentCalendarDay/$currentCalendarMonth ?"

        )
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.optimize_warning)
        dialogBuilder.setPositiveButton("Yes, optimize"){
                dialog, _ ->
            showProgressDialog("Optimizing..")
            dialog.cancel()
            EventsOptimization().calculateCombinations(eventsDurations,480.0,ArrayList(),maxEvents,this)
            combinations.forEach {
                val events = EventsOptimization().createEventsOrderedListByDurations(events,it)
                if (!combinationsOfSortedEventsLists.contains(events)){
                    combinationsOfSortedEventsLists.add(events)
                }
            }
            createOptimizedEventsDialog(combinationsOfSortedEventsLists)
            hideProgressDialog()
        }
        dialogBuilder.setNegativeButton("Maybe later, thanks"){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    fun optimizationSuccess(){

        combinations.clear()
        combinationsOfSortedEventsLists.clear()
        FirestoreClass().getEventsFromFirestore(this,currentCalendarDay,currentCalendarMonth)
        Thread.sleep(500)
        recyclerView.adapter!!.notifyDataSetChanged()
        hideProgressDialog()
        showErrorSnackBar("Optimization was successfully completed !",false)

    }
    private fun showAlertDialog1(){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("  Events Optimization")
        dialogBuilder.setMessage(
            "Law-Tech app can help you to optimize your events on $currentCalendarDay/$currentCalendarMonth \n\n" +
                    "Do you want Law-Tech app to optimize your events ?"
        )
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.optimize_warning)
        dialogBuilder.setPositiveButton("Yes, continue"){
                dialog, _ ->
            dialog.cancel()
            showAlertDialog2()
        }
        dialogBuilder.setNegativeButton("No thanks"){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}