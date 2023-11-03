package com.example.mystore.network.category

import com.example.mystore.models.category.CategoryCreateModel
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.models.category.CategoryUpdateModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiCategoryQueries {
    @GET("/categories")
    fun getCategories(): Call<MutableList<CategoryModel>>

    @GET("/categories/{id}")
    fun getCategory(@Path("id") id: Int): Call<CategoryModel>

    @POST("/categories/create")
    fun createCategory(@Body model: CategoryCreateModel): Call<CategoryModel>

    @PUT("/categories/update")
    fun updateCategory(@Body model: CategoryUpdateModel): Call<CategoryModel>

    @DELETE("/categories/delete/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Unit>
}