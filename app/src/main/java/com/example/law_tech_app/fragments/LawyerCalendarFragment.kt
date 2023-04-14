package com.example.law_tech_app.fragments

import android.content.Intent
import android.hardware.ConsumerIrManager.CarrierFrequencyRange
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Priority
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.AddEventActivity
import com.example.law_tech_app.adapters.EventListAdapter
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.models.EventType
import com.example.law_tech_app.models.Frequency
import com.example.law_tech_app.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_lawyer_calendar.*
import java.sql.Time
import java.time.DayOfWeek
import java.util.Calendar

class LawyerCalendarFragment :BaseFragment() {
    private lateinit var calendar:CalendarView
    private lateinit var events:ArrayList<Event>
    private lateinit var addEvent:FloatingActionButton
    private var currentCalendarDay = 0
    private var currentCalendarMonth = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_lawyer_calendar, container, false)
        calendar = fragView.findViewById(R.id.calendar_lawyerFragment)
        calendar.setOnDateChangeListener{ calendar,year,month,day->
            //TODO must add +1 to month
                currentCalendarDay = day
                currentCalendarMonth = month + 1
            FirestoreClass().getEventsFromFirestore(this,day,month+1)
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
        return fragView
    }
    fun deleteEventSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Event was successfully canceled !",false)
    }
    fun addEventSuccess(){
        showErrorSnackBar("Event was successfully added !",false)
    }
    fun updateEventSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Event was successfully updated !",false)
    }
    fun loadingEventsSuccess(eventsList:ArrayList<Event>){
        events = eventsList
        hideProgressDialog()
        if (eventsList.size > 0){
            rv_lawyerFragment_meetings.visibility = View.VISIBLE
            tv_lawyerFragment_meetings_noEvents.visibility = View.GONE
            rv_lawyerFragment_meetings.layoutManager = LinearLayoutManager(activity)
            rv_lawyerFragment_meetings.setHasFixedSize(true)
            val eventDetailsActivityAdapter = EventListAdapter(requireActivity(),eventsList,this)
            rv_lawyerFragment_meetings.adapter = eventDetailsActivityAdapter
        }else{
            rv_lawyerFragment_meetings.visibility = View.GONE
            tv_lawyerFragment_meetings_noEvents.visibility = View.VISIBLE
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

}