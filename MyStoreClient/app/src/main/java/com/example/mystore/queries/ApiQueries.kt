package com.example.mystore.queries

import com.example.mystore.models.CategoryModel
import retrofit2.http.GET

interface ApiQueries {
    @GET("api/categories")
    fun getCategories(): List<CategoryModel>
}