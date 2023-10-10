package com.example.mystore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.adapters.CustomAdapter
import com.example.mystore.model.CategoryModel
import com.example.mystore.retrofit.ApiCommon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        private const val imageUrl = "http://vpd121.itstep.click/images/1.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ApiCommon.service.getCategories().enqueue(object : Callback<MutableList<CategoryModel>> {
            override fun onResponse(
                call: Call<MutableList<CategoryModel>>,
                response: Response<MutableList<CategoryModel>>
            ) {
                val categories = response.body() ?: listOf()
                val recyclerView = findViewById<RecyclerView>(R.id.recycler)
                recyclerView.adapter = CustomAdapter(categories)
            }

            override fun onFailure(call: Call<MutableList<CategoryModel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                println(t.message)
            }
        })
    }
}