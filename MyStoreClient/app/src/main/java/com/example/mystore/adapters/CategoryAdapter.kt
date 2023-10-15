package com.example.mystore.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.R
import com.example.mystore.activities.category.CategoryUpdateActivity
import com.example.mystore.models.CategoryModel
import com.squareup.picasso.Picasso


class CategoryAdapter(
    private val data: List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        val image: ImageView?
            get() = itemView.findViewById(R.id.picture)

        val header: TextView?
            get() = itemView.findViewById(R.id.header)

        val subHeader: TextView?
            get() = itemView.findViewById(R.id.sub_header)
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.categoryitem_layout,
                parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = data[position]
        val context = holder.itemView.context
        val b = Bundle()
        b.putSerializable("model", currentItem)

        holder.header?.text = currentItem.name
        holder.subHeader?.text = currentItem.description
        try {
            Picasso.get().load(data[position].image).into(holder.image)
        } catch (exception: Exception) {
            /* ignore */
        }

        holder.itemView.findViewById<Button>(R.id.updateButton)
            .setOnClickListener {
                val intent = Intent(context, CategoryUpdateActivity::class.java)
                intent.putExtras(b)
                context.startActivity(intent)
            }
    }
}