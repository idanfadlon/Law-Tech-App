package com.example.law_tech_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.law_tech_app.CategoryData
import com.example.law_tech_app.R
import kotlinx.android.synthetic.main.search_category_layout.view.*


class CategoryAdapter(val context: Context, var cList:ArrayList<CategoryData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_category_layout , parent, false )
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {

        return cList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = cList[position]
        holder.itemView.tv_title.text = model.title
        holder.itemView.iv_category_icon.setImageResource(model.icon)
    }

    fun setFilteredList(cList: ArrayList<CategoryData>) {
        this.cList = cList
        notifyDataSetChanged()


    }


    class CategoryViewHolder(categoryView: View):RecyclerView.ViewHolder(categoryView){
        val ivCategoryIcon : ImageView = categoryView.findViewById(R.id.iv_category_icon)
        val tvCategoryTitle : TextView = categoryView.findViewById(R.id.tv_title)

    }

}

private fun ImageView.setImageDrawable(icon: Int) {

}
