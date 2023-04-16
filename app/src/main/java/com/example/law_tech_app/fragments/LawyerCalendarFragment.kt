package com.example.law_tech_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.AddEventActivity
import com.example.law_tech_app.adapters.EventListAdapter
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_lawyer_calendar.*
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
        return fragView
    }
    private fun selectedCalendarDateInRange(year:Int, month:Int, day:Int) : Boolean{
        val totalSelected = (year*360) + (month*30) + day
        val totalCurrent = ((Calendar.getInstance().get(Calendar.YEAR))*360) +
                (((Calendar.getInstance().get(Calendar.MONTH))+1)*30) + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        Log.e("matan",(totalSelected - totalCurrent).toString())
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