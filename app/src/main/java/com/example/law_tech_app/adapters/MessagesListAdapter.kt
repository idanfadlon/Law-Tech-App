package com.example.law_tech_app.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.R
import com.example.law_tech_app.activities.MessageDetailsActivity
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import kotlinx.android.synthetic.main.messages_lawyer_list.view.*

class MessagesListAdapter(
    val context:Context,
    var messagesList:ArrayList<Message>
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.messages_lawyer_list,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val model = messagesList[position]
        if (holder is MyViewHolder)
        {
            if(model.senderImageURL != ""){
            GlideLoader(context).loadSenderPicture(model.senderImageURL,holder.itemView.iv_sender_image)
            }else{
                GlideLoader(context).loadSenderPicture("",holder.itemView.iv_sender_image)
            }
            if (model.isReaden){
               holder.itemView.tv_messageDetailsActivity_subject.text = " Subject:" + " " + model.subject
                holder.itemView.tv_messageDetailsActivity_sender.text = " Sender:" + " " + model.sender
                holder.itemView.tv_messageDetailsActivity_status.text = " Status:" + ""
                val image:ImageView = holder.itemView.iv_icon_readen
                image.setImageResource(R.drawable.messagereaden)
            }else{
                val subject:TextView =  holder.itemView.tv_messageDetailsActivity_subject
                subject.text = " Subject:" + " " + model.subject
                subject.setTypeface(subject.typeface,Typeface.BOLD)
                val sender:TextView = holder.itemView.tv_messageDetailsActivity_sender
                sender.text = " Sender:" + " " + model.sender
                sender.setTypeface(sender.typeface,Typeface.BOLD)
                val status:TextView = holder.itemView.tv_messageDetailsActivity_status
                status.text = " Status:" + ""
                status.setTypeface(status.typeface,Typeface.BOLD)
                val image:ImageView = holder.itemView.iv_icon_readen
                image.setImageResource(R.drawable.message_incoming)
            }
            holder.itemView.setOnClickListener{
                //TODO implement the message as readen
                val intent = Intent(context,MessageDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_MESSAGE_DETAILS,model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return  messagesList.size
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}