package com.example.law_tech_app.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.models.Lawyer
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.Constants
import com.example.law_tech_app.utils.GlideLoader
import kotlinx.android.synthetic.main.dialog_message_details.*
import kotlinx.android.synthetic.main.dialog_send_message.*
import kotlinx.android.synthetic.main.dialog_user_profile_details.*
import kotlinx.android.synthetic.main.messages_lawyer_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessagesListAdapterClient(
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
            LayoutInflater.from(context).inflate(R.layout.messages_client_list,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val message = messagesList[position]
        if (holder is MyViewHolder)
        {

            GlideLoader(context).loadPicture(message.senderImageURL,holder.itemView.iv_sender_image)
            holder.itemView.tv_messageDetailsActivity_subject.text = " Subject:" + " " + message.subject
            holder.itemView.tv_messageDetailsActivity_sender.text = " Sender:" + " " + message.senderFullname
            holder.itemView.tv_messageDetailsActivity_time.text = " " + message.date
            holder.itemView.setOnClickListener{
                createMessageDetailsDialog(message)
            }
            holder.itemView.iv_icon_reply.setOnClickListener {
                createSendMessageDialog(message)
            }
            holder.itemView.iv_icon_delete.setOnClickListener {
                showDeleteAlertDialog("Delete Message","Are you sure you want to delete this message ?",message.id,position)
            }
            holder.itemView.iv_sender_image.setOnClickListener {
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
            val hashMap = HashMap<String,Any>()

            for(message in messagesList){
                if (message.senderUID == id){
                    messagesList.remove(message)
                }
            }
            notifyDataSetChanged()
            FirestoreClass().updateCurrentUserDetails(hashMap,Constants.CLIENTS,fragment)


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
        GlideLoader(userProfileDialog.context).loadCurrentUserPicture(user!!.imageURL,userProfileDialog.iv_user_profile_details_img)
        userProfileDialog.tv_user_profile_details_fullname.text = userProfileDialog.tv_user_profile_details_fullname.text.toString() + " " + user!!.fullName
        userProfileDialog.tv_user_profile_details_email.text =  userProfileDialog.tv_user_profile_details_email.text.toString() + " " + user!!.email
        userProfileDialog.tv_user_profile_details_phone.text = userProfileDialog.tv_user_profile_details_phone.text.toString() + " " + user!!.phoneNumber
        userProfileDialog.tv_user_profile_details_block.text = userProfileDialog.tv_user_profile_details_block.text.toString() + " " + user.fullName
        userProfileDialog.ib_user_profile_details_block.setOnClickListener {
        showBlockAlertDialog("Block User","Are you sure you want to block " + user!!.fullName +" ?\n\n" +
        "* Blocking " + user!!.fullName + " will remove all notifications he sent to you *", user!!.uid) }
        userProfileDialog.setCancelable(true)
        userProfileDialog.setCanceledOnTouchOutside(true)
        fragment.hideProgressDialog()
        userProfileDialog.show()

    }
    private fun createMessageDetailsDialog(message:Message){
        messageDetailsDialog = Dialog(context)
        messageDetailsDialog.setContentView(R.layout.dialog_message_details)
        messageDetailsDialog.tv_dialog_messageDetails_subject.text = messageDetailsDialog.tv_dialog_messageDetails_subject.text.toString() + " " + message.subject
        messageDetailsDialog.tv_dialog_messageDetails_sender.text =messageDetailsDialog.tv_dialog_messageDetails_sender.text.toString() + " " + message.senderFullname
        messageDetailsDialog.tv_dialog_messageDetails_body.text = messageDetailsDialog.tv_dialog_messageDetails_body.text.toString() + " " + message.messageBody
        messageDetailsDialog.tv_dialog_messageDetails_reply.text = messageDetailsDialog.tv_dialog_messageDetails_reply.text.toString() + " " + message.senderFullname
        messageDetailsDialog.tv_dialog_messageDetails_watch.text = "Watch " + message.senderFullname + "'s profile"
        GlideLoader(messageDetailsDialog.context).loadCurrentUserPicture(message.senderImageURL,messageDetailsDialog.iv_messageDetails_img)
        messageDetailsDialog.ib_dialog_messageDetails_reply.setOnClickListener {
            messageDetailsDialog.dismiss()
            createSendMessageDialog(message)
        }
        messageDetailsDialog.ib_dialog_messageDetails_watch.setOnClickListener {
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
        sendMessageDialog.tv_dialog_sendMessage_title.text = sendMessageDialog.tv_dialog_sendMessage_title.text.toString() + " " + message.senderFullname
        sendMessageDialog.tie_dialog_sendMessage_subject.setText("RE: " + message.subject)
        sendMessageDialog.ib_dialog_sendMessage_send.setOnClickListener {
            val dateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z")
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"))
            val today = Calendar.getInstance().time
            val msg = Message(
                "",
                sendMessageDialog.tie_dialog_sendMessage_subject.text.toString(),
                currentUser.uid,
                currentUser.fullName,
                currentUser.imageURL,
                message.senderUID,
                sendMessageDialog.tie_dialog_sendMessage_messageBody.text.toString(),
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