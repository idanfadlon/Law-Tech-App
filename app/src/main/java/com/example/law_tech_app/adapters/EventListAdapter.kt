package com.example.law_tech_app.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.BaseActivity
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.models.Event
import kotlinx.android.synthetic.main.dialog_event_details.*
import kotlinx.android.synthetic.main.dialog_update_event_details.*
import kotlinx.android.synthetic.main.events_lawyer_list.view.*

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
    private lateinit var freqsTypesSpinner: Spinner

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
        holder.itemView.tv_events_lawyer_subject.text = holder.itemView.tv_events_lawyer_subject.text.toString() + " " + event.subject
        holder.itemView.tv_events_lawyer_participants.text = holder.itemView.tv_events_lawyer_participants.text.toString() + " " + event.participants
        holder.itemView.tv_events_lawyer_time.text = holder.itemView.tv_events_lawyer_time.text.toString() + ""
        when(event.type){
            "Consultation" -> {
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_consultation)
            }
            "Documents Transfer"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_documents)
            }
            "Personal"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_personal)
            }
            "Court"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_court)
            }
            "Administrative"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_administrative)
            }
            "Company Event"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_company)
            }
            "Case Handling"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.icon_case)
            }
            "Other"->{
                holder.itemView.iv_events_lawyer_icon.setImageResource(R.drawable.ca)
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
        eventDetailsDialog.tv_dilaog_subject.text = event.subject
        eventDetailsDialog.tv_dilaog_participants.text =  eventDetailsDialog.tv_dilaog_participants.text.toString() + " " + event.participants
        eventDetailsDialog.tv_dilaog_duration.text = eventDetailsDialog.tv_dilaog_duration.text.toString() + " " + (event.duration * 100).toString() + " Min"
        eventDetailsDialog.tv_dilaog_description.text = eventDetailsDialog.tv_dilaog_description.text.toString()  + " " + event.description
        eventDetailsDialog.ib_dialog_google.setOnClickListener{
            openGoogleCalendar(event)
        }
        eventDetailsDialog.ib_dialog_update.setOnClickListener {
            createEventUpdateDetailsDialog(event,position)
            eventDetailsDialog.dismiss()
        }
        eventDetailsDialog.ib_dialog_delete.setOnClickListener {
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
        updateEventDialog.tie_updateDetails_subject.setText(event.subject)
        updateEventDialog.tie_updateDetails_description.setText(event.description)
        updateEventDialog.tie_updateDetails_duration.setText(event.duration.toString())
        updateEventDialog.tie_updateDetails_participants.setText(event.participants)
        updateEventDialog.ib_updateEvent_update.setOnClickListener {
            val eventHashMap = HashMap<String,Any>()
            eventHashMap["subject"] = updateEventDialog.tie_updateDetails_subject.text.toString()
            eventHashMap["description"] = updateEventDialog.tie_updateDetails_description.text.toString()
            eventHashMap["participants"] = updateEventDialog.tie_updateDetails_participants.text.toString()
            eventHashMap["duration"] = updateEventDialog.tie_updateDetails_duration.text.toString().toDouble()
            eventHashMap["frequency"] = updateEventDialog.spinner_updateDetails_freq.selectedItem.toString()
            eventHashMap["priority"]  = updateEventDialog.spinner_updateDetails_priority.selectedItem.toString()
            eventHashMap["type"] = updateEventDialog.spinner_updateDetails_type.selectedItem.toString()
            fragment.showProgressDialog("Loading..")
            updateEventDialog.dismiss()
            events.get(position).type = updateEventDialog.spinner_updateDetails_type.selectedItem.toString()
            notifyItemChanged(position)
            FirestoreClass().updateEvent(eventHashMap,event.id, fragment.requireActivity() as BaseActivity,fragment)

        }
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
        freqsTypesSpinner = updateEventDialog.findViewById(R.id.spinner_updateDetails_freq)
        ArrayAdapter.createFromResource(
            updateEventDialog.context,
            R.array.freqs_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            freqsTypesSpinner.adapter = adapter
            freqsTypesSpinner.setSelection(adapter.getPosition(event.frequency))
        }
        updateEventDialog.setCancelable(true)
        updateEventDialog.setCanceledOnTouchOutside(true)
        updateEventDialog.show()
    }

    override fun getItemCount(): Int {
       return events.size
    }

}