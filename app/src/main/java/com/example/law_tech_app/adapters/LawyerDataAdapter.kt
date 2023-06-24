package com.example.law_tech_app.adapters

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.LawyerData
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.BaseFragment
import com.example.law_tech_app.models.Message
import com.example.law_tech_app.models.User
import com.example.law_tech_app.utils.GlideLoader
import kotlinx.android.synthetic.main.dialog_send_message.*
import kotlinx.android.synthetic.main.search_lawyer_in_category_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LawyerDataAdapter(val context: Context, var lawyersList:ArrayList<LawyerData>,val currentUser:User, val fragment: BaseFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var sendMessageDialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.search_lawyer_in_category_layout, parent, false )
        return LawyerInCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {

        return lawyersList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = lawyersList[position]
        if (model.imgUrl!=""){
            GlideLoader(context).loadPicture(model.imgUrl as Any,holder.itemView.iv_lawyer_image)
            holder.itemView.tv_lawyer_name.text = model.name
            holder.itemView.tv_lawyer_about.text = model.about
            val isExpandable: Boolean = model.isExpandable
            holder.itemView.tv_lawyer_about.visibility = if (isExpandable) View.VISIBLE else View.GONE

            holder.itemView.cl_search_lawyer_in_category.setOnClickListener {
                model.isExpandable = !model.isExpandable
                notifyItemChanged(position)
            }

        }
        else{
            holder.itemView.tv_lawyer_name.text = model.name
            holder.itemView.tv_lawyer_about.text = model.about
            val isExpandable: Boolean = model.isExpandable
            holder.itemView.tv_lawyer_about.visibility = if (isExpandable) View.VISIBLE else View.GONE

            holder.itemView.cl_search_lawyer_in_category.setOnClickListener {
                model.isExpandable = !model.isExpandable
                notifyItemChanged(position)
            }

        }
        holder.itemView.ib_sendMessage.setOnClickListener {
            createSendMessageDialog()

        }
    }
    private fun createSendMessageDialog(){
        sendMessageDialog = Dialog(context)
        sendMessageDialog.setContentView(R.layout.dialog_send_message)
        sendMessageDialog.tv_dialog_sendMessage_title.text = sendMessageDialog.tv_dialog_sendMessage_title.text.toString() + " "
        sendMessageDialog.tie_dialog_sendMessage_subject.setText("")
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
                currentUser.uid,
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


    fun setFilteredList(lawyersList: ArrayList<LawyerData>) {
        this.lawyersList = lawyersList
        notifyDataSetChanged()


    }


    class LawyerInCategoryViewHolder(lawyerIncategoryView: View): RecyclerView.ViewHolder(lawyerIncategoryView){
        val ivLawyerImage : ImageView = lawyerIncategoryView.findViewById(R.id.iv_lawyer_image)
        val tvLawyerName : TextView = lawyerIncategoryView.findViewById(R.id.tv_lawyer_name)

    }

}