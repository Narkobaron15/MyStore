package com.example.mystore

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystore.model.CategoryModel
import com.example.mystore.retrofit.ApiCommon
import com.squareup.picasso.Picasso

// https://habr.com/ru/articles/520544/

class MainActivity : AppCompatActivity() {
    companion object {
        private const val imageUrl = "http://vpd121.itstep.click/images/1.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.picPlaceholder)

        Picasso.get().load(imageUrl).into(imageView)
//        Glide.with(HomeApplication.getAppContext()).load(imageUrl).into(imageView)

        ApiCommon.service.getCategories().enqueue(object : retrofit2.Callback<MutableList<CategoryModel>> {
            override fun onResponse(
                call: retrofit2.Call<MutableList<CategoryModel>>,
                response: retrofit2.Response<MutableList<CategoryModel>>
            ) {
                val categories = response.body()

                if (categories != null) {
                    for (category in categories) {
                        println(category.name)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<MutableList<CategoryModel>>, t: Throwable) {
                println(t.message)
            }
        })
    }
}