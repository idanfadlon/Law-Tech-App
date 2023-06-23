package com.example.law_tech_app.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.LawyerData
import com.example.law_tech_app.R
import com.example.law_tech_app.utils.GlideLoader
import kotlinx.android.synthetic.main.search_lawyer_in_category_layout.view.*

class LawyerDataAdapter(val context: Context, var lawyersList:ArrayList<LawyerData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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