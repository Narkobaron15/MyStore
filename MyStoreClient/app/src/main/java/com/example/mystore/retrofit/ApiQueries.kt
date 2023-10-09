package com.example.mystore.retrofit

import com.example.mystore.model.CategoryModel
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiQueries {
    @GET("/api/categories/list")
    fun getCategories(): Call<MutableList<CategoryModel>>
}