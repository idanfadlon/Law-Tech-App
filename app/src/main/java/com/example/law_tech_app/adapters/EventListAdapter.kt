package com.example.law_tech_app.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.EventUpdateDetailsActivity
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.utils.Constants
import kotlinx.android.synthetic.main.dialog_event_details.*
import kotlinx.android.synthetic.main.events_lawyer_list.view.*

class EventListAdapter(
    val context: Context,
    val events:ArrayList<Event>
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var dialog : Dialog

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
        //TODO implement the binding holder
        val event = events[position]
        holder.itemView.tv_events_lawyer_subject.text = holder.itemView.tv_events_lawyer_subject.text.toString() + " " + event.subject
        for(participant in event.participants){
            holder.itemView.tv_events_lawyer_participants.text = holder.itemView.tv_events_lawyer_subject.text.toString() + " " + participant + " "
        }
        holder.itemView.tv_events_lawyer_time.text = holder.itemView.tv_events_lawyer_time.text.toString() + event.date
        holder.itemView.setOnClickListener{

            dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_event_details)
            dialog.tv_dilaog_subject.text = event.subject
            for (participant in event.participants){
                dialog.tv_dilaog_participants.text = dialog.tv_dilaog_participants.text.toString() + " " + participant + " "
            }
            dialog.tv_dilaog_duration.text = dialog.tv_dilaog_duration.text.toString() + " " +event.date //TODO update attribute date to duration / time
            dialog.tv_dilaog_description.text = dialog.tv_dilaog_description.text.toString()  + " " + "something"
            dialog.ib_dialog_google.setOnClickListener{
                openGoogleCalendar(event)
            }
            dialog.ib_dialog_update.setOnClickListener {
                val intent = Intent(context, EventUpdateDetailsActivity::class.java )
                intent.putExtra(Constants.EXTRA_EVENT_DETAILS,event)
                dialog.dismiss()
                context.startActivity(intent)
            }
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()

        }
    }

    private fun openGoogleCalendar(meeting:Event){
    //TODO implement google add event
        dialog.dismiss()
    }

    override fun getItemCount(): Int {
       return events.size
    }

}