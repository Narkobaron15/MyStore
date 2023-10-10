package com.example.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mystore.R
import com.example.mystore.model.CategoryModel
import com.squareup.picasso.Picasso


class CustomAdapter(
    private val cnt : Context,
    private val data: List<CategoryModel>
) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(cnt)

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return data.size
    }

    override fun getItem(position: Int): CategoryModel? {
        // TODO Auto-generated method stub
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var vi = convertView ?: inflater.inflate(R.layout.custom_list_item, null)

        val headerView = vi.findViewById<TextView>(R.id.header)
        headerView.text = getItem(position)?.name ?: ""

        val subHeaderView = vi.findViewById<TextView>(R.id.sub_header)
        subHeaderView.text = getItem(position)?.description ?: ""

        val imgView = vi.findViewById<ImageView>(R.id.picture)
        val imageUrl = getItem(position)?.image ?: ""
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(imageUrl).into(imgView)
        }

        return vi
    }
}