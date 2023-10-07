package com.example.mystore

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystore.application.HomeApplication
import com.example.mystore.queries.ApiQueries
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    companion object {
        private const val imageUrl = "http://10.0.2.2:5047/api/uploads/1.jpg"
        private const val apiUrl = "http://localhost:5047/"

        private val service = Retrofit.Builder()
            .baseUrl(apiUrl)
            .build()
            .create(ApiQueries::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.picPlaceholder)

//        Picasso.get().load(imageUrl).into(imageView);
        Glide.with(HomeApplication.getAppContext()).load(imageUrl).into(imageView)
    }
}