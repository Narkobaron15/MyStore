package com.example.mystore.adapters

import android.app.AlertDialog
import android.content.Context
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
import com.example.mystore.activities.MainActivity
import com.example.mystore.activities.category.CategoryUpdateActivity
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            Picasso.get().load(currentItem.image).into(holder.image)
        } catch (exception: Exception) {
            /* ignore */
        }

        holder.itemView.findViewById<Button>(R.id.updateButton)
            .setOnClickListener {
                val intent = Intent(
                    context,
                    CategoryUpdateActivity::class.java
                )
                intent.putExtras(b)
                context.startActivity(intent)
            }

        holder.itemView.findViewById<Button>(R.id.deleteButton)
            .setOnClickListener {
                createDeleteDialog(
                    currentItem,
                    holder.itemView,
                    context
                ).show()
            }
    }

    private fun createDeleteDialog(
        currentItem: CategoryModel,
        view: View,
        context: Context
    ) : AlertDialog {
        val builder = AlertDialog.Builder(context)
        val failSnackbar = Snackbar.make(
            view,
            "Failed to delete category",
            Snackbar.LENGTH_SHORT
        )
        val callback = object: Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (!response.isSuccessful)
                {
                    failSnackbar.show()
                    return
                }

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }

            override fun onFailure(
                call: Call<Unit>,
                t: Throwable
            ) = failSnackbar.show()
        }

        builder.setMessage("Are you sure you want to delete this category?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                ApiClient.categoryService.deleteCategory(currentItem.id!!)
                    .enqueue(callback)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }
}