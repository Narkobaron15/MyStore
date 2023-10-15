package com.example.mystore.network

import com.example.mystore.models.CategoryCreateModel
import com.example.mystore.models.CategoryModel
import com.example.mystore.models.CategoryUpdateModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiCategoryQueries {
    @GET("/api/categories/list")
    fun getCategories(): Call<MutableList<CategoryModel>>

    @POST("/api/categories/create")
    fun createCategory(@Body model: CategoryCreateModel): Call<CategoryModel>

    @PUT("/api/categories/update")
    fun updateCategory(@Body model: CategoryUpdateModel): Call<CategoryModel>

    @DELETE("/api/categories/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Unit>
}