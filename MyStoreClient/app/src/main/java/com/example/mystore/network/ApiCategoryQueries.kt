package com.example.mystore.network

import com.example.mystore.models.CategoryCreateModel
import com.example.mystore.models.CategoryModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiCategoryQueries {
    @GET("/api/categories/list")
    fun getCategories(): Call<MutableList<CategoryModel>>

    @POST("/api/categories/create")
    fun createCategory(@Body model: CategoryCreateModel): Call<CategoryModel>
}