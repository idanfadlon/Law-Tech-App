package com.example.law_tech_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CalendarView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.AddEventActivity
import com.example.law_tech_app.adapters.EventListAdapter
import com.example.law_tech_app.models.Event
import kotlinx.android.synthetic.main.fragment_lawyer_calendar.*

class LawyerCalendarFragment :BaseFragment() {
    private lateinit var calendar:CalendarView

    private lateinit var events:ArrayList<Event>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_lawyer_calendar, container, false)
        calendar = fragView.findViewById(R.id.calendar_lawyerFragment)
        calendar.setOnDateChangeListener{ calendar,year,month,day ->
            //TODO must add +1 to month
        }
        return fragView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lawyer_fragment_menu_calendar,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.icon_add_event->{
                val intent = Intent(activity, AddEventActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun loadingMeetingSuccess(eventsList:ArrayList<Event>){
        eventsList.add(
            Event(
            "123456",
            "matan",
            ArrayList(),
                "Eating Pizza with Idan",
                "Infinity"
        )
        )
        events = eventsList
        hideProgressDialog()
        if (eventsList.size > 0){
            rv_lawyerFragment_meetings.visibility = View.VISIBLE
            tv_lawyerFragment_meetings_noEvents.visibility = View.GONE
            rv_lawyerFragment_meetings.layoutManager = LinearLayoutManager(activity)
            rv_lawyerFragment_meetings.setHasFixedSize(true)
            val eventDetailsActivityAdapter = EventListAdapter(requireActivity(),eventsList)
            val dividerItemDecoration = DividerItemDecoration(rv_lawyerFragment_meetings.context,
                LinearLayoutManager.VERTICAL)
            rv_lawyerFragment_meetings.addItemDecoration(dividerItemDecoration)
            rv_lawyerFragment_meetings.adapter = eventDetailsActivityAdapter
        }else{
            rv_lawyerFragment_meetings.visibility = View.GONE
            tv_lawyerFragment_meetings_noEvents.visibility = View.VISIBLE
        }
    }
    private fun getMeetingsListFromFirestore(){
        showProgressDialog(resources.getString(R.string.loading))
        loadingMeetingSuccess(ArrayList())
        hideProgressDialog()
    }
    override fun onResume() {
        super.onResume()
        getMeetingsListFromFirestore()
    }

}