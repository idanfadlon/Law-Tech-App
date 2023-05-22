package com.example.law_tech_app.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.models.Client
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessagesListAdapter(
    val context:Context,
    var messagesList:ArrayList<Message>,
    val currentUser:User,
    val fragment: BaseFragment
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    lateinit var messageDetailsDialog:Dialog
    lateinit var sendMessageDialog:Dialog
    lateinit var userProfileDialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.messages_lawyer_list,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val message = messagesList[position]
        if (holder is MyViewHolder)
        {
            val senderImage = holder.itemView.findViewById<ImageView>(R.id.iv_sender_image)
            GlideLoader(context).loadSenderPicture(message.senderImageURL,senderImage)
            val tvSubject = holder.itemView.findViewById<TextView>(R.id.tv_message_subject)
            tvSubject.text = " Subject:" + " " + message.subject
            val tvSender = holder.itemView.findViewById<TextView>(R.id.tv_message_sender)
            tvSender.text = " Sender:" + " " + message.senderFullname
            val tvTime = holder.itemView.findViewById<TextView>(R.id.tv_message_time)
            tvTime.text = " " + message.date
            holder.itemView.setOnClickListener{
                createMessageDetailsDialog(message)
            }
            val ivReply = holder.itemView.findViewById<ImageView>(R.id.iv_icon_reply)
            ivReply.setOnClickListener {
                createSendMessageDialog(message)
            }
            val ivDelete = holder.itemView.findViewById<ImageView>(R.id.iv_icon_delete)
            ivDelete.setOnClickListener {
                showDeleteAlertDialog("Delete Message","Are you sure you want to delete this message ?",message.id,position)
            }

            senderImage.setOnClickListener {
                fragment.showProgressDialog("Loading..")
                FirestoreClass().getUserFromFirestore(message.senderUID,Constants.CLIENTS,this)
            }

        }
    }
    private fun showDeleteAlertDialog(title:String, message:String, id:String, position: Int){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.warning)
        dialogBuilder.setPositiveButton(R.string.yes){
                _, _ ->
                fragment.showProgressDialog("Loading..")
                FirestoreClass().deleteMessageFromFirestore(id,fragment)
                notifyItemRemoved(position)
        }
        dialogBuilder.setNegativeButton(R.string.no){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    private fun showBlockAlertDialog(title:String, message:String, id:String){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setIcon(R.drawable.warning)
        dialogBuilder.setPositiveButton(R.string.yes){
                _, _ ->
            fragment.showProgressDialog("Loading..")
            userProfileDialog.dismiss()
            val currentUserBlockedList = (currentUser as Lawyer).blockedClients
            currentUserBlockedList.add(id)
            val hashMap = HashMap<String,Any>()
            hashMap[Constants.BLOCKED_CLIENTS] = currentUserBlockedList
            for(msg in messagesList){
                if (msg.senderUID == id){
                    messagesList.remove(msg)
                    break
                }
            }
            notifyDataSetChanged()
            FirestoreClass().updateCurrentUserDetails(hashMap,Constants.LAWYERS,fragment)

        }
        dialogBuilder.setNegativeButton(R.string.no){
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun createUserProfileDialog(user: User){
        userProfileDialog = Dialog(context)
        userProfileDialog.setContentView(R.layout.dialog_user_profile_details)
        val ivUser = userProfileDialog.findViewById<ImageView>(R.id.iv_user_profile_details_img)
        GlideLoader(userProfileDialog.context).loadCurrentUserPicture(user.imageURL,ivUser)
        val tvFullname = userProfileDialog.findViewById<TextView>(R.id.tv_user_profile_details_fullname)
        tvFullname.text = tvFullname.text.toString() + " " + user!!.fullName
        val tvEmail = userProfileDialog.findViewById<TextView>(R.id.tv_user_profile_details_email)
        tvEmail.text =  tvEmail.text.toString() + " " + user.email
        val tvPhone = userProfileDialog.findViewById<TextView>(R.id.tv_user_profile_details_phone)
        tvPhone.text = tvPhone.text.toString() + " " + user!!.phoneNumber
        val tvBlock = userProfileDialog.findViewById<TextView>(R.id.tv_user_profile_details_block)
        tvBlock.text = tvBlock.text.toString() + " " + user.fullName
        val blockBtn =  userProfileDialog.findViewById<ImageButton>(R.id.ib_user_profile_details_block)
        blockBtn.setOnClickListener {
        showBlockAlertDialog("Block User","Are you sure you want to block " + user.fullName +" ?\n\n" +
        "* Blocking " + user.fullName + " will remove all notifications he sent to you *", user.uid) }
        userProfileDialog.setCancelable(true)
        userProfileDialog.setCanceledOnTouchOutside(true)
        fragment.hideProgressDialog()
        userProfileDialog.show()

    }
    private fun createMessageDetailsDialog(message:Message){
        messageDetailsDialog = Dialog(context)
        messageDetailsDialog.setContentView(R.layout.dialog_message_details)
        val tvSubject = messageDetailsDialog.findViewById<TextView>(R.id.tv_dialog_messageDetails_subject)
        tvSubject.text = tvSubject.text.toString() + " " + message.subject
        val tvSender = messageDetailsDialog.findViewById<TextView>(R.id.tv_dialog_messageDetails_sender)
        tvSender.text =tvSender.text.toString() + " " + message.senderFullname
        val tvBody = messageDetailsDialog.findViewById<TextView>(R.id.tv_dialog_messageDetails_body)
        tvBody.text = tvBody.text.toString() + " " + message.messageBody
        val tvReply = messageDetailsDialog.findViewById<TextView>(R.id.tv_dialog_messageDetails_reply)
        tvReply.text = tvReply.text.toString() + " " + message.senderFullname
        val tvWatch = messageDetailsDialog.findViewById<TextView>(R.id.tv_dialog_messageDetails_watch)
        tvWatch.text = "Watch " + message.senderFullname + "'s profile"
        val ivSender = messageDetailsDialog.findViewById<ImageView>(R.id.iv_dialog_messageDetails_image)
        GlideLoader(messageDetailsDialog.context).loadCurrentUserPicture(message.senderImageURL,ivSender)
        val replyBtn = messageDetailsDialog.findViewById<ImageButton>(R.id.ib_dialog_messageDetails_reply)
        replyBtn.setOnClickListener {
            messageDetailsDialog.dismiss()
            createSendMessageDialog(message)
        }
        val watchBtn = messageDetailsDialog.findViewById<ImageButton>(R.id.ib_dialog_messageDetails_watch)
        watchBtn.setOnClickListener {
            messageDetailsDialog.dismiss()
            fragment.showProgressDialog("Loading..")
            FirestoreClass().getUserFromFirestore(message.senderUID,Constants.CLIENTS,this)
        }
        messageDetailsDialog.setCancelable(true)
        messageDetailsDialog.setCanceledOnTouchOutside(true)
        messageDetailsDialog.show()
    }
    private fun createSendMessageDialog(message:Message){
        sendMessageDialog = Dialog(context)
        sendMessageDialog.setContentView(R.layout.dialog_send_message)
        val tvTitle = sendMessageDialog.findViewById<TextView>(R.id.tv_dialog_sendMessage_title)
        tvTitle.text = tvTitle.text.toString() + " " + message.senderFullname
        val tieSubject = sendMessageDialog.findViewById<TextInputEditText>(R.id.tie_dialog_sendMessage_subject)
        tieSubject.setText("RE: " + message.subject)
        val tieBody = sendMessageDialog.findViewById<TextInputEditText>(R.id.tie_dialog_sendMessage_messageBody)
        val sendBtn = sendMessageDialog.findViewById<ImageButton>(R.id.ib_dialog_sendMessage_send)
        sendBtn.setOnClickListener {
            val dateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z")
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"))
            val today = Calendar.getInstance().time
            val msg = Message(
                "",
                tieSubject.text.toString(),
                currentUser.uid,
                currentUser.fullName,
                currentUser.imageURL,
                message.senderUID,
                tieBody.text.toString(),
                dateFormat.format(today).slice(IntRange(0,21)),
                false
            )
            fragment.showProgressDialog("Loading..")
            FirestoreClass().addMessageToFirestore(msg,fragment)
            sendMessageDialog.dismiss()

        }
        sendMessageDialog.setCancelable(true)
        sendMessageDialog.setCanceledOnTouchOutside(true)
        sendMessageDialog.show()
    }

    override fun getItemCount(): Int {
        return  messagesList.size
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}