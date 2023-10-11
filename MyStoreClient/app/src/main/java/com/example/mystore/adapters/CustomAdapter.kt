package com.example.mystore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.R
import com.example.mystore.model.CategoryModel
import com.squareup.picasso.Picasso

class CustomAdapter(
    private val data: List<CategoryModel>
) : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView?
            get() = itemView.findViewById(R.id.picture)

        val header: TextView?
            get() = itemView.findViewById(R.id.header)

        val subHeader: TextView?
            get() = itemView.findViewById(R.id.sub_header)
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_list_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        println(holder)
        holder.header?.text = data[position].name
        holder.subHeader?.text = data[position].description
        Picasso.get().load(data[position].image).into(holder.image)
    }
}