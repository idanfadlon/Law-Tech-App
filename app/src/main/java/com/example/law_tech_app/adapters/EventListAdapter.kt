package com.example.law_tech_app.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.BaseActivity
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.models.Event
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar


class EventListAdapter(
    val context: Context,
    val events:ArrayList<Event>,
    val fragment: BaseFragment
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var eventDetailsDialog : Dialog
    private lateinit var updateEventDialog: Dialog
    private lateinit var prioritySpinner: Spinner
    private lateinit var eventTypesSpinner: Spinner
    private lateinit var calendarStart:Calendar
    private lateinit var calendarEnd:Calendar

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessagesListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.events_lawyer_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]
        val tvSubject = holder.itemView.findViewById<TextView>(R.id.tv_events_lawyer_subject)
        tvSubject.text = tvSubject.text.toString() + " " + event.subject
        val tvParticipants = holder.itemView.findViewById<TextView>(R.id.tv_events_lawyer_participants)
        tvParticipants.text = tvParticipants.text.toString() + " " + event.participants
        val tvTime = holder.itemView.findViewById<TextView>(R.id.tv_events_lawyer_time)
        tvTime.text = tvTime.text.toString() + "  " + event.time
        val iv =  holder.itemView.findViewById<ImageView>(R.id.iv_events_lawyer_icon)
        when(event.type){
            "Consultation" -> {
                iv.setImageResource(R.drawable.icon_consultation)
            }
            "Documents Transfer"->{
                iv.setImageResource(R.drawable.icon_documents)
            }
            "Personal"->{
                iv.setImageResource(R.drawable.icon_personal)
            }
            "Court"->{
                iv.setImageResource(R.drawable.icon_court)
            }
            "Administrative"->{
                iv.setImageResource(R.drawable.icon_administrative)
            }
            "Company Event"->{
                iv.setImageResource(R.drawable.icon_company)
            }
            "Case Handling"->{
                iv.setImageResource(R.drawable.icon_case)
            }
            "Other"->{
                iv.setImageResource(R.drawable.ca)
            }
        }
        holder.itemView.setOnClickListener{
            createEventDetailsDialog(event,position)
        }
    }

    private fun openGoogleCalendar(event:Event){
    //TODO implement google add event
        eventDetailsDialog.dismiss()
    }

    fun createEventDetailsDialog(event: Event,position: Int){
        eventDetailsDialog = Dialog(context)
        eventDetailsDialog.setContentView(R.layout.dialog_event_details)
        val tvSubject = eventDetailsDialog.findViewById<TextView>(R.id.tv_dilaog_subject)
        tvSubject.text = event.subject
        val tvParticipants = eventDetailsDialog.findViewById<TextView>(R.id.tv_dilaog_participants)
        tvParticipants.text =  tvParticipants.text.toString() + " " + event.participants
        val tvDuration = eventDetailsDialog.findViewById<TextView>(R.id.tv_dilaog_duration)
        tvDuration.text = tvDuration.text.toString() + " " + (event.duration).toString() + " Min(s)"
        val tvTime = eventDetailsDialog.findViewById<TextView>(R.id.tv_dilaog_time)
        tvTime.text = tvTime.text.toString() + " " + event.eventDay.toString() + "/" + event.eventMonth.toString() + " at " + event.time
        val tvDescription = eventDetailsDialog.findViewById<TextView>(R.id.tv_dilaog_description)
        tvDescription.text = tvDescription.text.toString() + " " + event.description
        val googleBtn = eventDetailsDialog.findViewById<ImageButton>(R.id.ib_dialog_google)
        googleBtn.setOnClickListener{
            openGoogleCalendar(event)
        }
        val updateBtn = eventDetailsDialog.findViewById<ImageButton>(R.id.ib_dialog_update)
        updateBtn.setOnClickListener {
            createEventUpdateDetailsDialog(event,position)
            eventDetailsDialog.dismiss()
        }
        val cancelBtn = eventDetailsDialog.findViewById<ImageButton>(R.id.ib_dialog_delete)
        cancelBtn.setOnClickListener {
            showAlertDialog("Cancel Event","Are you sure you want to cancel this event ?",event.id,position)
        }
        eventDetailsDialog.setCancelable(true)
        eventDetailsDialog.setCanceledOnTouchOutside(true)
        eventDetailsDialog.show()
    }
    private fun showAlertDialog(title:String, message:String,id:String,position: Int){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.warning)
        dialogBuilder.setPositiveButton(R.string.yes){
                dialog, _ ->
                fragment.showProgressDialog("Loading..")
                dialog.cancel()
                eventDetailsDialog.dismiss()
                events.removeAt(position)
                notifyItemRemoved(position)
                FirestoreClass().deleteEventFromFirestore(id, fragment)

        }
        dialogBuilder.setNegativeButton(R.string.no){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    private fun createEventUpdateDetailsDialog(event: Event,position: Int){
        updateEventDialog = Dialog(context)
        updateEventDialog.setContentView(R.layout.dialog_update_event_details)
        val tieSubject = updateEventDialog.findViewById<TextInputEditText>(R.id.tie_updateDetails_subject)
        tieSubject.setText(event.subject)
        val tieDescription = updateEventDialog.findViewById<TextInputEditText>(R.id.tie_updateDetails_description)
        tieDescription.setText(event.description)
        val pickTimeStart = updateEventDialog.findViewById<Button>(R.id.btn_updateDetails_pickTime_start)
        pickTimeStart.text = event.time.subSequence(0,5).toString()
        val tiePreparingDuration = updateEventDialog.findViewById<TextInputEditText>(R.id.tie_updateDetails_preparing)
        tiePreparingDuration.setText(event.preparingDuration.toString())
        pickTimeStart.setOnClickListener {
            calendarStart = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
                calendarStart.set(Calendar.HOUR_OF_DAY,hour)
                calendarStart.set(Calendar.MINUTE,minute)
                pickTimeStart.text = SimpleDateFormat("HH:mm").format(calendarStart.time)
            }
            TimePickerDialog(context,timeSetListener,calendarStart.get(Calendar.HOUR_OF_DAY),calendarStart.get(
                Calendar.MINUTE),true).show()
        }
        val pickTimeEnd = updateEventDialog.findViewById<Button>(R.id.btn_updateDetails_pickTime_end)
        pickTimeEnd.text = event.time.subSequence(6,11).toString()
        pickTimeEnd.setOnClickListener {
            calendarEnd = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
                calendarEnd.set(Calendar.HOUR_OF_DAY,hour)
                calendarEnd.set(Calendar.MINUTE,minute)
                pickTimeEnd.text = SimpleDateFormat("HH:mm").format(calendarEnd.time)
            }
            TimePickerDialog(context,timeSetListener,calendarEnd.get(Calendar.HOUR_OF_DAY),calendarEnd.get(
                Calendar.MINUTE),true).show()
        }

        val tieParticipants = updateEventDialog.findViewById<TextInputEditText>(R.id.tie_updateDetails_participants)
        tieParticipants.setText(event.participants)
        val updateBtn = updateEventDialog.findViewById<ImageButton>(R.id.ib_updateEvent_update)

        prioritySpinner = updateEventDialog.findViewById(R.id.spinner_updateDetails_priority)
        ArrayAdapter.createFromResource(
            updateEventDialog.context,
            R.array.priority_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            prioritySpinner.adapter = adapter
            prioritySpinner.setSelection(adapter.getPosition(event.priority))
        }

        eventTypesSpinner = updateEventDialog.findViewById(R.id.spinner_updateDetails_type)
        ArrayAdapter.createFromResource(
            updateEventDialog.context,
            R.array.types_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            eventTypesSpinner.adapter = adapter
            eventTypesSpinner.setSelection(adapter.getPosition(event.type))
        }

        updateBtn.setOnClickListener {
            val duration = ((calendarEnd.get(Calendar.HOUR_OF_DAY) - calendarStart.get(Calendar.HOUR_OF_DAY)) * 60) + (calendarEnd.get(Calendar.MINUTE) - calendarStart.get(Calendar.MINUTE))
            val eventHashMap = HashMap<String,Any>()
            eventHashMap["subject"] = tieSubject.text.toString()
            eventHashMap["description"] = tieDescription.text.toString()
            eventHashMap["participants"] = tieParticipants.text.toString()
            eventHashMap["preparingDuration"] = tiePreparingDuration.text.toString().toDouble()
            eventHashMap["priority"]  = prioritySpinner.selectedItem.toString()
            eventHashMap["type"] = eventTypesSpinner.selectedItem.toString()
            eventHashMap["time"] = SimpleDateFormat("HH:mm").format(calendarStart.time) + "-" + SimpleDateFormat("HH:mm").format(calendarEnd.time)
            eventHashMap["duration"] = duration.toDouble()
            fragment.showProgressDialog("Loading..")
            updateEventDialog.dismiss()
            events[position].type = eventTypesSpinner.selectedItem.toString()
            events[position].time = SimpleDateFormat("HH:mm").format(calendarStart.time) + "-" + SimpleDateFormat("HH:mm").format(calendarEnd.time)
            events[position].participants = tieParticipants.text.toString()
            events[position].preparingDuration = tiePreparingDuration.text.toString().toDouble()
            events[position].subject = tieSubject.text.toString()
            events[position].description = tieDescription.text.toString()
            events[position].duration = duration.toDouble()
            events[position].priority = prioritySpinner.selectedItem.toString()

            notifyItemChanged(position)
            FirestoreClass().updateEvent(eventHashMap,event.id, fragment.requireActivity() as BaseActivity,fragment)

        }

        updateEventDialog.setCancelable(true)
        updateEventDialog.setCanceledOnTouchOutside(true)
        updateEventDialog.show()
    }

    override fun getItemCount(): Int {
       return events.size
    }

}